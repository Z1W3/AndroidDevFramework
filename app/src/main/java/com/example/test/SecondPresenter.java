package com.example.test;

import com.catt.mvp.presenter.AbstractPresenter;

public class SecondPresenter extends AbstractPresenter implements IMainActivity.ISecondPresenter {
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
        iView.onChangeText2("这是第二个,num = " + num++);
    }
}
