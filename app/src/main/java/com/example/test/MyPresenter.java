package com.example.test;

import android.util.Log;

import z1w3.mvp.support.BasePresenter;

public class MyPresenter extends BasePresenter implements MyPresenterAPI{

    private MainViewAPI viewAPI;


    @Override
    public void onCreate() {
        super.onCreate();
        viewAPI = getViewAPI();
        Log.e("TAG", "onCreate activity=" + getActivity() + ", context="+getContext());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy activity=" + getActivity() + ", context="+getContext());
    }

    @Override
    public void fetchText() {
        viewAPI.setText("触发了 MyPresenter#fetchText()");
    }
}
