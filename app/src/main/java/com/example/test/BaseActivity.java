package com.example.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import z1w3.mvp.support.SupportMVP;

class BaseActivity extends AppCompatActivity {

    private final SupportMVP supportMVP = new SupportMVP();

    public BaseActivity(){
        super();
        supportMVP.attach(this);
    }


    protected <T> T getPresenterAPI(Class<T> cls){
        return supportMVP.getPresenterAPI(cls);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportMVP.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        supportMVP.detach();
    }
}
