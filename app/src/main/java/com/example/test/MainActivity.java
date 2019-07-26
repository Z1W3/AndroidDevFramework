package com.example.test;

import android.os.Bundle;

import com.catt.mvp.BaseActivity;
import com.catt.mvp.delegated.BaseActivityDelegated;

public class MainActivity extends BaseActivity {

    @Override
    protected BaseActivityDelegated injectDelegated() {
        return new MainView();
    }

}
