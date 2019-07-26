package com.catt.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.lang.ref.Reference;

/**
 * @author:         支玮
 * @createDate:     2019-07-18 14:04
 * @description:
 *
 * P层抽象类
 *
 * example:
 * <pre>
 *      public class MyPresenter extends AbstractPresenter implement IPresenterInterface{
 *          private IViewInterface iView;
 *
 *          @Override
 *          protected void onCreate(){
 *              iView = getViewInterface();
 *          }
 *      }
 * <pre/>
 *
 */
public abstract class AbstractPresenter {

    private Object view;
    private volatile Reference refActivity;

    private volatile Reference refFragment;

    public Context getContext() {
        if(refFragment != null && refFragment.get() != null){
            return ((Fragment) refFragment.get()).getContext();
        }
        if(refActivity != null && refActivity.get() != null){
            return ((Activity) refActivity.get());
        }
        return null;
    }

    public Activity getActivity(){
        if(refFragment != null && refFragment.get() != null){
            return ((Fragment) refFragment.get()).getActivity();
        }
        if(refActivity != null && refActivity.get() != null){
            return ((Activity) refActivity.get());
        }
        return null;
    }

    protected <V> V getViewInterface() {
        return (V) view;
    }

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 14:18
     * @description:
     *
     * P层与V层进行真实绑定的方法
     */
    public <BA extends Activity> void onAttachActivity(Class<?> declaredClazz, Object o, Reference<BA> refActivity) {
        final Object cast = declaredClazz.cast(o);
        if (cast == null) {
            throw new ClassCastException("view convert errot");
        }
        this.view = cast;
        this.refActivity = refActivity;
        onCreate();
    }

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 14:18
     * @description:
     *
     * P层与V层进行真实绑定的方法
     */
    public <BF extends Fragment> void onAttachFragment(Class<?> declaredClazz, Object o, Reference<BF> refFragment) {
        final Object cast = declaredClazz.cast(o);
        if (cast == null) {
            throw new ClassCastException("view convert errot");
        }
        this.view = cast;
        this.refFragment = refFragment;
        onCreate();
    }


    public void onDetach() {
        onDestroy();
    }

    protected abstract void onCreate();

    /**
     * 调用此方法后，应该销毁所有操作
     */
    protected abstract void onDestroy();
}
