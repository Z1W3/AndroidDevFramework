package com.example.test.main;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.test.BaseActivity;
import com.example.test.MyPresenter;
import com.example.test.MyPresenterAPI;
import com.example.test.R;
import com.example.test.first.FirstFragment;

import z1w3.mvp.support.annotations.InjectPresenter;

@InjectPresenter(values = {MyPresenter.class})
public class MainActivity extends BaseActivity implements MainViewAPI {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new FirstFragment(), "FirstFragment").commit();
    }

    public void submit(View view) {
        getPresenterAPI(MyPresenterAPI.class).fetchText();

    }

    @Override
    public void setText(String content) {
        textView.setText(content);
    }

}
