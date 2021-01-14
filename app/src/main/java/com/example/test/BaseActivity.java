package com.example.test;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import z1w3.mvp.support.MVPHelper;

public class BaseActivity extends AppCompatActivity {

    private final MVPHelper MVPHelper = new MVPHelper();


    protected <T> T getPresenterAPI(Class<T> cls){
        return MVPHelper.getPresenterAPI(cls);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MVPHelper.attach(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MVPHelper.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MVPHelper.detach();
    }
}
