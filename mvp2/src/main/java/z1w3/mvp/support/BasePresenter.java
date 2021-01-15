package z1w3.mvp.support;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 子类必须是public访问权限，否则无法被实例
 */
public abstract class BasePresenter {

    private Map<Class<?>, Object> otherPresenterMap;

    private Object viewAPI;
    private Object target;
    private Context applicationContext;


    public void attach(Class<?> viewAPIClazz, Object target, Map<Class<?>, Object> otherPresenterMap) {
        final Object cast = viewAPIClazz.cast(target);
        if (cast == null) {
            throw new ClassCastException("View-API convert error");
        }
        this.otherPresenterMap = copyMap(otherPresenterMap);
        this.viewAPI = cast;
        this.target = target;
        applicationContext = getContext();
    }

    protected Context getApplicationContext() {
        return applicationContext;
    }

    public void onCreate(){

    }

    public void onDestroy() {
        viewAPI = null;
        target = null;
        applicationContext = null;
        otherPresenterMap = null;
    }

    protected <V> V getViewAPI() {
        return (V) viewAPI;
    }

    protected Context getContext() {
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
