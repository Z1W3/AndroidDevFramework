package com.example.test;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import z1w3.mvp.support.annotations.MultiPresenter;

@MultiPresenter(values = {MyPresenter.class})
public class MainActivity extends BaseActivity implements MainViewAPI {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);
    }

    public void submit(View view) {
        getPresenterAPI(MyPresenterAPI.class).fetchText();
    }

    @Override
    public void setText(String content) {
        textView.setText(content);
    }
}
