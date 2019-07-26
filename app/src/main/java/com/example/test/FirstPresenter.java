package com.example.test;

import com.catt.mvp.presenter.AbstractPresenter;

public class FirstPresenter extends AbstractPresenter implements IMainActivity.IFirstPresenter {
    private IMainActivity.IView iView;
    @Override
    protected void onCreate() {
        iView = getViewInterface();
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    public void testMethod() {
        iView.onChangeText1("这是第一个");
    }
}
