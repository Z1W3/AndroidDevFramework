package com.catt.mvp.delegated;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.catt.mvp.exception.DelegatedException;
import com.catt.mvp.presenter.AbstractPresenter;
import com.catt.mvp.stack.FragmentStack;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * @author: 支玮
 * @createDate: 2019-07-18 13:59
 * @description: Fragment委托抽象类, 范型传递与之被代理的Fragment类
 * example:
 * <pre>
 *      class MyFragmentDelegatedImpl extends BaseFragmentDelegated<MyFragment> implements IViewInterface{
 *          ...
 *          private final IPresenterInterface iPresenter = getPresenterInterface();
 *      }
 * <pre/>
 *
 * 传递被代理Fragment类的目的，
 * 主要是通过Fragment堆栈管理器提取该Fragment的对象，
 * 其次明确代理与被代理类的关系
 *
 *
 * 所有Fragment将不在处理主要业务逻辑，而是将一切委托至委托类中进行实现，
 * 这样每个被实例的Fragment，都可以根据传递来的arguments信息选择创建不同的委托实现类
 *
 * 与委托类关联的被委托类(Fragment)会被SoftReference进行引用，防止内存益处
 */
public abstract class BaseFragmentDelegated<T extends Fragment> extends AbstractViewDelegated implements IDelegatedFragment {

    private Type[] declaredType;

    private volatile Reference<T> reference;


    {
        ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        declaredType = superclass.getActualTypeArguments();
    }


    @Override
    protected Activity getActivity() {
        return reference.get().getActivity();
    }

    @Override
    protected Context getContext() {
        return reference.get().getActivity().getApplicationContext();
    }

    @Override
    public Fragment getFragment() {
        return reference.get();
    }

    @Override
    public void onCreate() {
        final Fragment fragment = FragmentStack.INSTANCE.peekFragment((Class<T>) declaredType[0]);
        if (fragment == null) {
            throw new DelegatedException("no search fragment");
        }
        reference = new SoftReference(fragment);

        for (AbstractPresenter abstractPresenter : getAbstractPresenterArray()) {
            abstractPresenter.onAttachFragment(super.getDeclaredViewClass(), this, reference);
        }
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public void onDestroy() {
        for (AbstractPresenter abstractPresenter : getAbstractPresenterArray()) {
            abstractPresenter.onDetach();
        }
        reference.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroyView() {

    }


    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onViewLoadCompleted() {

    }
}
