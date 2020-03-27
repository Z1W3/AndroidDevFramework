package com.catt.mvp.delegated;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.catt.mvp.exception.DelegatedException;
import com.catt.mvp.presenter.AbstractPresenter;
import com.catt.mvp.stack.ActivityStack;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author:         支玮
 * @createDate:     2019-07-18 13:32
 * @description:
 *
 * Activity委托抽象类,范型传递与之被代理的Activity类
 * example:
 * <pre>
 *      class MyActivityDelegatedImpl extends BaseActivityDelegated<MyActivity> implements IViewInterface{
 *          ...
 *          private final IPresenterInterface iPresenter = getPresenterInterface();
 *      }
 * <pre/>
 *
 * 传递被代理Activity类的目的，
 * 主要是通过Activity堆栈管理器提取该Activity的对象，
 * 其次明确代理与被代理类的关系
 *
 * 类的作用：
 * 所有Activity将不在处理主要业务逻辑，而是将一切委托至委托类中进行实现，
 * 这样每个被实例的Activity，都可以根据传递来的Intent信息选择创建不同的委托实现类
 *
 * 与委托类关联的被委托类(Activity)会被SoftReference进行引用，防止内存益处
 */
public abstract class BaseActivityDelegated<T extends Activity> extends AbstractViewDelegated implements IDelegatedActivity {

    private Type[] declaredType;

    private volatile Reference<T> reference;

    {
        ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        declaredType = superclass.getActualTypeArguments();
    }

    @Override
    protected Activity getActivity() {
        return reference.get();
    }

    @Override
    protected Context getContext() {
        return reference.get().getApplicationContext();
    }

    @Override
    public void onCreate() {
        final Activity activity = ActivityStack.INSTANCE.searchActivity((Class<T>) declaredType[0]);
        if (activity == null) {
            throw new DelegatedException("no search activity");
        }
        reference = new SoftReference(activity);
        for (AbstractPresenter abstractPresenter : getAbstractPresenterArray()) {
            abstractPresenter.onAttachActivity(super.getDeclaredViewClass(), this, reference);
        }
    }

    @Override
    public <V extends View> V findViewById(int id) {
        return getActivity().findViewById(id);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onDestroy() {
        for (AbstractPresenter abstractPresenter : getAbstractPresenterArray()) {
            abstractPresenter.onDetach();
        }
        reference.clear();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onViewLoadCompleted() {

    }
}
