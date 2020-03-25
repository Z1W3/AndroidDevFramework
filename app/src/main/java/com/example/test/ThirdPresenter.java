package com.example.test;

import com.catt.mvp.presenter.AbstractPresenter;

public class ThirdPresenter extends AbstractPresenter implements IMainActivity.IThirdPresenter {

    private IMainActivity.IView iView;

    private int num = 0;

    @Override
    protected void onCreate() {
        iView = getViewInterface();
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    public void testMethod() {
        iView.onChangeText3("这是第三个,num = " + num++);
    }
}
