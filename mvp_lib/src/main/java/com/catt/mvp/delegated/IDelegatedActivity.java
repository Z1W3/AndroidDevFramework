package com.catt.mvp.delegated;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author:         支玮
 * @createDate:     2019-07-18 13:07
 * @description:
 * 继承基础接口，并在基础接口的基础上增加Activity中特有的方法。
 */
public interface IDelegatedActivity extends IDelegatedView {

    View findViewById(int id);

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 13:09
     * @description:
     * 这个方法将会在Activity执行完setContentView(..)方法后触发
     * 所以android.view.View的初始化工作可以在此方法中进行
     */
    void onViewCreated();

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 13:10
     * @description:
     * 这个方法将会在本接口的onViewCreate()方法执行完毕之后触发
     * 数据的初始化可以放在此方法中进行
     *
     * @param savedInstanceState
     * 方法中的Bundle参数可以帮助恢复一些必要的数据设置。
     * 但是需要注意的是，任何主动退出都不能帮助你恢复数据，
     * 如何使用可以google一下，关于onSaveInstanceState(..)方法,数据恢复
     */
    void onInitData(@Nullable Bundle savedInstanceState);

    void onNewIntent(Intent intent);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState);

}
