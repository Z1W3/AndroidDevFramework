package com.catt.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.catt.mvp.delegated.BaseFragmentDelegated;
import com.catt.mvp.stack.FragmentStack;


/**
 * @author:         支玮
 * @createDate:     2019-07-18 14:25
 * @description:
 *
 * 将Fragment的业务逻辑完全委托给委托类进行处理，
 * 委托类可以传递来的arguments进行动态创建。
 *
 * Fragment的实现类中可以定制特殊的方法共委托类使用,
 * 也可以在BaseFragment中设置公共方法供委托类使用
 *
 * example:
 * <pre>
 *
 * <pre/>
 */
public abstract class BaseFragment extends Fragment {

    private BaseFragmentDelegated delegated;

    abstract protected BaseFragmentDelegated injectDelegated();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegated.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(delegated.getLayoutResID(), container, false);
    }

    private boolean isVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delegated.onViewCreated(view);
        view.post(new Runnable() {
            @Override
            public void run() {
                if (delegated != null) {
                    delegated.onViewLoadCompleted();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        delegated.onInitData(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        delegated.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        delegated.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        delegated.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        delegated.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        delegated.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        delegated.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FragmentStack.INSTANCE.push(this);
        delegated = injectDelegated();
        delegated.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        delegated.onDetach();
        delegated = null;
        FragmentStack.INSTANCE.remove(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        delegated.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        delegated.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        delegated.onViewStateRestored(savedInstanceState);
    }
}
