package z1w3.mvp.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import z1w3.mvp.support.annotations.MultiPresenter;
import z1w3.mvp.support.annotations.PresenterAPI;
import z1w3.mvp.support.annotations.ViewAPI;
import z1w3.mvp.support.exception.PresenterException;

public class SupportMVP {

    private Map<Class<?>, Object> presenterInterfaceMap;

    /**
     * Presenter层实例对象
     */
    private BasePresenter[] presenterArray;
    /**
     * P层接口 Class类
     */
    private Class<?>[] presenterApiClazz;

    public void attach(Object obj) {
        try {
            final MultiPresenter annotation = getMultiInjectPresenter(obj);
            presenterArray = newPresenterArray(annotation);
            presenterApiClazz = getPresenterAPIClassArray(presenterArray);
            presenterInterfaceMap = getPresenterInterfaceMap();
            Class<?> viewAPIClass = getViewAPIClass(obj);
            invokePresenterMethod("attach", new Class[]{Class.class, Object.class}, new Object[]{viewAPIClass, obj});
        } catch (PresenterException e) {
            e.printStackTrace();
        }
    }

    private void invokePresenterMethod(String name, Class<?>[] parameterTypes, Object[] args){
        try {
            for (BasePresenter basePresenter : presenterArray) {
                final Class<? extends BasePresenter> clazz = basePresenter.getClass();
                final Method method = clazz.getMethod(name, parameterTypes);
                method.setAccessible(true);
                method.invoke(basePresenter, args);
                method.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onCreate() {
        invokePresenterMethod("onCreate", new Class[0], new Object[0]);
    }

    public void detach() {
        invokePresenterMethod("onDestroy", new Class[0], new Object[0]);
    }

    private <P> Map<Class<?>, P> getPresenterInterfaceMap(){
        final Map<Class<?>, P> map = new HashMap<>();
        for (int index = 0; index < presenterArray.length; index++) {
            final Class<?> presenterApiClazz = this.presenterApiClazz[index];
            final BasePresenter presenter = presenterArray[index];
            final Object cast = presenterApiClazz.cast(presenter);
            if(cast == null){
                continue;
            }
            for (Class<?> apiClazz : cast.getClass().getInterfaces()) {
                final PresenterAPI annotation = apiClazz.getAnnotation(PresenterAPI.class);
                if (annotation != null) {
                    map.put(apiClazz, (P) cast);
                }
            }
        }
        return map;
    }

    public <T> T getPresenterAPI(Class<T> cls){
        if(presenterInterfaceMap.isEmpty()){
            return null;
        }
        final Object obj = presenterInterfaceMap.get(cls);
        if (obj == null){
            return null;
        }
        return (T) obj;
    }

    private Class<?> getPresenterAPIClass(Object o) throws PresenterException {
        for (Class<?> anInterface : o.getClass().getInterfaces()) {
            final PresenterAPI annotation = anInterface.getAnnotation(PresenterAPI.class);
            if (annotation != null) {
                return o.getClass().asSubclass(anInterface);
            }
        }
        throw new PresenterException("Must use declaration annotation class DeclaredIPresenter");
    }

    private Class<?> getViewAPIClass(Object o) throws PresenterException {
        for (Class<?> anInterface : o.getClass().getInterfaces()) {
            final ViewAPI annotation = anInterface.getAnnotation(ViewAPI.class);
            if (annotation != null) {
                return o.getClass().asSubclass(anInterface);
            }
        }
        throw new PresenterException("Must use declaration annotation class DeclaredIView");
    }


    /**
     * 获取注解
     * @param o
     * @return
     */
    private MultiPresenter getMultiInjectPresenter(Object o){
        return o.getClass().getAnnotation(MultiPresenter.class);
    }

    /**
     * 实例Presenter类
     * 将注解中的Presenter全部进行实例
     */
    private BasePresenter[] newPresenterArray(MultiPresenter annotation){
        final Class<? extends BasePresenter>[] values = annotation.values();
        final BasePresenter[] presenterArray = new BasePresenter[values.length];
        for (int index = 0; index < values.length; index++) {
            try {
                final String name = values[index].getName();
                final BasePresenter abstractPresenter = (BasePresenter) Class.forName(name).newInstance();
                presenterArray[index] = abstractPresenter;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return presenterArray;
    }


    private Class<?>[] getPresenterAPIClassArray(BasePresenter[] presenterArray) throws PresenterException {
        Class<?>[] declaredPresenterClassArray = new Class[presenterArray.length];
        for (int index = 0; index < presenterArray.length; index++) {
            final Class<?> declaredPresenterClass = getPresenterAPIClass(presenterArray[index]);
            declaredPresenterClassArray[index] = declaredPresenterClass;
        }
        return declaredPresenterClassArray;
    }
}
