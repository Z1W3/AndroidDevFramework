package com.example.test;

import android.view.View;
import android.widget.Button;

import com.catt.mvp.annotations.InjectMultiPresenter;
import com.catt.mvp.delegated.BaseActivityDelegated;

import java.util.Map;


@InjectMultiPresenter(
        values = {
                FirstPresenter.class,
                SecondPresenter.class,
                ThirdPresenter.class
})
public class MainView extends BaseActivityDelegated<MainActivity>
        implements IMainActivity.IView {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main;
    }

    private Button btn_first;
    private Button btn_second;
    private Button btn_third;
    @Override
    public void onViewCreated() {
        super.onViewCreated();
        btn_first = findViewById(R.id.btn_first);
        btn_first.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final IMainActivity.IFirstPresenter iFirstPresenter = getIPresenter(IMainActivity.IFirstPresenter.class);
                        if (iFirstPresenter != null){
                            iFirstPresenter.testMethod();
                        }
                    }
                }
        );
        btn_second = findViewById(R.id.btn_second);
        btn_second.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final IMainActivity.ISecondPresenter iSecondPresenter = getIPresenter(IMainActivity.ISecondPresenter.class);
                        if (iSecondPresenter != null){
                            iSecondPresenter.testMethod();
                        }
                    }
                }
        );
        btn_third = findViewById(R.id.btn_third);
        btn_third.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final IMainActivity.IThirdPresenter iThirdPresenter = getIPresenter(IMainActivity.IThirdPresenter.class);
                        if (iThirdPresenter != null){
                            iThirdPresenter.testMethod();
                        }
                    }
                }
        );
    }

    @Override
    public void onChangeText1(String content) {
        btn_first.setText(content);
    }

    @Override
    public void onChangeText2(String content) {
        btn_second.setText(content);

    }

    @Override
    public void onChangeText3(String content) {
        btn_third.setText(content);

    }
}
