package z1w3.mvp.support;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

/**
 * 子类必须是public访问权限，否则无法被实例
 */
public abstract class BasePresenter {

    private Map<Class<?>, Object> otherPresenterMap;

    private Object viewAPI;
    private Object target;

    public void attach(Class<?> viewAPIClazz, Object target, Map<Class<?>, Object> otherPresenterMap) {
        final Object cast = viewAPIClazz.cast(target);
        if (cast == null) {
            throw new ClassCastException("View-API convert error");
        }
        for (Map.Entry<Class<?>, Object> entry : otherPresenterMap.entrySet()) {
            if (entry.getValue() == this) {
                otherPresenterMap.remove(entry.getKey());
            }
        }
        this.otherPresenterMap = otherPresenterMap;
        this.viewAPI = cast;
        this.target = target;
        applicationContext = getContext();
    }

    private Context applicationContext;

    protected Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected void onCreate(){

    }

    protected void onDestroy() {
        viewAPI = null;
        target = null;
        applicationContext = null;
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
}
