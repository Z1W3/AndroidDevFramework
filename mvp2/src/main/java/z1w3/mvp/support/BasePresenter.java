package z1w3.mvp.support;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * 子类必须是public访问权限，否则无法被实例
 */
public abstract class BasePresenter {
    private Object viewAPI;
    private Object target;

    protected <V> V getViewAPI() {
        return (V) viewAPI;
    }

    protected Context getContext() {
        if (target == null) {
            return null;
        }
        if (target instanceof Fragment) {
            return ((Fragment) target).getContext();
        }
        if (target instanceof Activity) {
            if (!((Activity) target).isDestroyed()) {
                return ((Activity) target);
            }
        }
        return null;
    }

    protected Activity getActivity() {
        if (target == null) {
            return null;
        }
        if (target instanceof Fragment) {
            return ((Fragment) target).getActivity();
        }
        if (target instanceof Activity) {
            if (!((Activity) target).isDestroyed()) {
                return ((Activity) target);
            }
        }
        return null;
    }

    public void attach(Class<?> viewAPIClazz, Object target) {
        final Object cast = viewAPIClazz.cast(target);
        if (cast == null) {
            throw new ClassCastException("View-API convert error");
        }
        this.viewAPI = cast;
        this.target = target;
    }

    public void onCreate(){

    }

    public void onDestroy() {
        viewAPI = null;
        target = null;
    }
}
