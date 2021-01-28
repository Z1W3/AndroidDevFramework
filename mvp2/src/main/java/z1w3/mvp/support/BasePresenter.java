package z1w3.mvp.support;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import z1w3.mvp.support.annotations.Singleton;

/**
 * 子类必须是public访问权限，否则无法被实例
 */
public abstract class BasePresenter {

    private Map<Class<?>, Object> otherPresenterMap;

    private Object viewAPI;
    private Object target;
    private Context applicationContext;


    public void attach(Class<?> viewAPIClazz, Object target, Map<Class<?>, Object> otherPresenterMap) throws ClassCastException {
        final Object cast = viewAPIClazz.cast(target);
        if (cast == null) {
            throw new ClassCastException("View-API convert error");
        }
        this.otherPresenterMap = copyMap(otherPresenterMap);
        this.viewAPI = cast;
        this.target = target;
        final Context context = getContext();
        if(context != null){
            applicationContext = context.getApplicationContext();
            final Singleton annotation = this.getClass().getAnnotation(Singleton.class);
            if(annotation != null){
                this.target = null;
            }
        }
    }

    protected Context getApplicationContext() {
        return applicationContext;
    }

    public void onCreate(){

    }

    public void onDestroy() {
        final Singleton annotation = this.getClass().getAnnotation(Singleton.class);
        if(annotation != null){
            Log.w("Presenter", "Current is Singleton Presenter, Cannot be destroyed.");
            return;
        }
        viewAPI = null;
        target = null;
        applicationContext = null;
        otherPresenterMap = null;
    }

    protected <V> V getViewAPI() {
        return (V) viewAPI;
    }

    protected Context getContext() {
        final Singleton annotation = this.getClass().getAnnotation(Singleton.class);
        if(annotation != null && applicationContext != null){
            Log.w("Presenter", "Current is Singleton Presenter, Only provide ApplicationContext.");
            return applicationContext;
        }

        if (target == null) {
            return null;
        }

        Activity activity = null;
        if (target instanceof android.app.Fragment) {
            activity = ((android.app.Fragment) target).getActivity();
        }
        if (target instanceof android.support.v4.app.Fragment) {
            activity = ((android.support.v4.app.Fragment) target).getActivity();
        }
        if (target instanceof Activity) {
            activity = (Activity) target;
        }
        if (activity != null && !activity.isDestroyed()) {
            return activity;
        }
        return null;
    }

    protected <T> T getOtherPresenterAPI(Class<T> cls) {
        final Singleton annotation = this.getClass().getAnnotation(Singleton.class);
        if(annotation != null){
            Log.e("Presenter", "Current is Singleton Presenter, Not recommended for use 'getOtherPresenterAPI' method. Because the current 'View' may not be the object that initializes the Singleton Presenter. Origin 'View' >>> " + viewAPI);
        }
        if (otherPresenterMap == null || otherPresenterMap.isEmpty()) {
            return null;
        }
        final Object obj = otherPresenterMap.get(cls);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    private Map<Class<?>, Object> copyMap(Map<Class<?>, Object> otherPresenterMap){
        final HashMap<Class<?>, Object> map = new HashMap<>(otherPresenterMap);
        final ArrayList<Class<?>> list = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> entry : map.entrySet()) {
            if (entry.getValue() == this) {
                list.add(entry.getKey());
            }
        }
        for (Class<?> clazz : list) {
            map.remove(clazz);
        }
        return map;
    }


}
