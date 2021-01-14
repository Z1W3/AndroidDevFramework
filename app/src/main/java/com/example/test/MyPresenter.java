package com.example.test;

import android.util.Log;

import com.example.test.first.FirstViewAPI;
import com.example.test.main.MainViewAPI;

import z1w3.mvp.support.BasePresenter;

public class MyPresenter extends BasePresenter implements MyPresenterAPI {

    private MainViewAPI mainViewAPI;
    private FirstViewAPI firstViewAPI;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate >>> " + "context=" + getContext());
        final Object viewAPI = getViewAPI();
        if (viewAPI != null) {
            if (viewAPI instanceof MainViewAPI) {
                mainViewAPI = (MainViewAPI) viewAPI;
            }
            if (viewAPI instanceof FirstViewAPI) {
                firstViewAPI = (FirstViewAPI) viewAPI;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy >>> " + "context=" + getContext());
    }

    private int number = 0;

    @Override
    public void fetchText() {
        if (mainViewAPI != null) {
            mainViewAPI.setText("触发了 MyPresenter#fetchText() Activity" + (++number));
        }
        if (firstViewAPI != null) {
            firstViewAPI.setText("触发了 MyPresenter#fetchText() Fragment" + (++number));
        }
    }
}
