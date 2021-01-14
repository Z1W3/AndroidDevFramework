package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import z1w3.mvp.support.MVPHelper;

public class BaseFragment extends Fragment {

    private final MVPHelper mvpHelper = new MVPHelper();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mvpHelper.attach(this);
    }

    protected <T> T getPresenterAPI(Class<T> cls) {
        return mvpHelper.getPresenterAPI(cls);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpHelper.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvpHelper.detach();
    }
}
