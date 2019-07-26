package com.catt.mvp.delegated;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * @author:         支玮
 * @createDate:     2019-07-18 13:16
 * @description:
 * 继承基础接口，并在基础接口的基础上增加Fragment中特有的方法。
 */
public interface IDelegatedFragment extends IDelegatedView {

    void onSaveInstanceState(Bundle outState);

    void onViewStateRestored(Bundle savedInstanceState);

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 13:17
     * @description:
     * 这个接口方法会在Fragment中onViewCreated(..)方法中触发
     * 用于初始化android.view.View
     *
     * @param view
     */
    void onViewCreated(View view);

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 13:19
     * @description:
     * 这个接口方法会在Fragment中onActivityCreated(..)方法中触发
     * 用于初始化数据
     *
     * @param savedInstanceState
     */
    void onInitData(@Nullable Bundle savedInstanceState);

    void onDestroyView();

    void onAttach(Context context);

    void onDetach();

    Fragment getFragment();
}
