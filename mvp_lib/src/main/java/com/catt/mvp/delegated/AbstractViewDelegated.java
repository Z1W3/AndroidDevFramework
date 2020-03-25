package com.catt.mvp.delegated;

import android.app.Activity;
import android.content.Context;

import com.catt.mvp.annotations.DeclaredIPresenter;
import com.catt.mvp.annotations.DeclaredIView;
import com.catt.mvp.annotations.InjectMultiPresenter;
import com.catt.mvp.exception.DelegatedException;
import com.catt.mvp.presenter.AbstractPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:         支玮
 * @createDate:     2019-07-18 12:48
 * @description:
 *
 * V层委托抽象类
 * 这个类用于解析@InjectMultiPresenter/@DeclaredIPresenter/@DeclaredIView
 *
 * @InjectPresenter
 * 这个注解将V层委托类与P层业务类进行绑定，
 * 注解中的value需要填写P层实现类全类名，
 * example: com.a.b.presenter.DemoPresenterImpl
 *
 * @DeclaredIView
 * 解析本抽象类的继承者（子类）的实现接口,
 * 并在P层类被实例化后将V层接口传递至P层，使之P层类可以获取V层接口
 *
 * @DeclaredIPresenter
 * P层类被实例化后，会对P层类实现接口进行解析，
 * 使V层委托类可以获取P层接口。
 *
 * 以上三种注解解析完毕后，会将V层与P层进行绑定，
 * 并且P层实现类持有V层委托实现类接口，V层委托实现类持有P层接口，
 * 从而达到双方接口互调的目的
 *
 */
public abstract class AbstractViewDelegated {

    private Map<Class, Object> presenterInterfaceMap;

    /**
     * Presenter层实例对象
     */
    private AbstractPresenter[] abstractPresenters;
    /**
     * P层接口 Class类
     */
    private Class<?>[] declaredPresenterClzs;
    /**
     * V层接口 Class类
     */
    private Class<?> declaredViewClass;

    {
        try {
            final InjectMultiPresenter annotation = AbstractViewDelegated.getMultiInjectPresenter(this);
            abstractPresenters = AbstractViewDelegated.newInstancePresenters(annotation);
            declaredPresenterClzs = AbstractViewDelegated.getDeclaredPresenterClassArray(abstractPresenters);
            declaredViewClass = AbstractViewDelegated.getDeclaredViewClass(this);
            presenterInterfaceMap = getPresenterInterfaceMap();
        } catch (DelegatedException e) {
            e.printStackTrace();
        }
    }

    abstract public int getLayoutResID();
    abstract protected Context getContext();
    abstract protected Activity getActivity();

    protected Class<?> getDeclaredViewClass(){
        return declaredViewClass;
    }

    protected AbstractPresenter[] getAbstractPresenterArray() {
        return abstractPresenters;
    }

    private <P> Map<Class, P> getPresenterInterfaceMap(){
        final Map<Class, P> map = new HashMap<>();
        for (int index = 0; index < abstractPresenters.length; index++) {
            final Class<?> declaredPresenterClz = declaredPresenterClzs[index];
            final AbstractPresenter abstractPresenter = abstractPresenters[index];
            final Object cast = declaredPresenterClz.cast(abstractPresenter);
            if(cast == null){
                continue;
            }
            for (Class<?> anInterface : cast.getClass().getInterfaces()) {
                final DeclaredIPresenter annotation = anInterface.getAnnotation(DeclaredIPresenter.class);
                if (annotation != null) {
                    map.put(anInterface, (P) cast);
                }
            }
        }
        return map;
    }

    protected <T> T getIPresenter(Class<T> cls){
        if(presenterInterfaceMap.isEmpty()){
            return null;
        }
        final Object obj = presenterInterfaceMap.get(cls);
        if (obj == null){
            return null;
        }
        return (T) obj;
    }

    private static Class<?> getDeclaredPresenterClass(Object o) throws DelegatedException {
        for (Class<?> anInterface : o.getClass().getInterfaces()) {
            final DeclaredIPresenter annotation = anInterface.getAnnotation(DeclaredIPresenter.class);
            if (annotation != null) {
                return o.getClass().asSubclass(anInterface);
            }
        }
        throw new DelegatedException("Must use declaration annotation class DeclaredIPresenter");
    }

    private static Class<?> getDeclaredViewClass(Object o) throws DelegatedException {
        for (Class<?> anInterface : o.getClass().getInterfaces()) {
            final DeclaredIView annotation = anInterface.getAnnotation(DeclaredIView.class);
            if (annotation != null) {
                return o.getClass().asSubclass(anInterface);
            }
        }
        throw new DelegatedException("Must use declaration annotation class DeclaredIView");
    }


    private static InjectMultiPresenter getMultiInjectPresenter(Object o){
        return o.getClass().getAnnotation(InjectMultiPresenter.class);
    }

    private static AbstractPresenter[] newInstancePresenters(InjectMultiPresenter annotation){
        final Class[] values = annotation.values();
        final AbstractPresenter[] abstractPresenters = new AbstractPresenter[values.length];
        for (int index = 0; index < values.length; index++) {
            try {
                final AbstractPresenter abstractPresenter = (AbstractPresenter) Class.forName(values[index].getName()).newInstance();
                abstractPresenters[index] = abstractPresenter;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return abstractPresenters;
    }


    private static Class<?>[] getDeclaredPresenterClassArray(Object[] oArray){
        Class<?>[] declaredPresenterClassArray = new Class[oArray.length];
        for (int index = 0; index < oArray.length; index++) {
            final Class<?> declaredPresenterClass = getDeclaredPresenterClass(oArray[index]);
            declaredPresenterClassArray[index] = declaredPresenterClass;
        }
        return declaredPresenterClassArray;
    }
}
