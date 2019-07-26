package com.example.test;

import android.view.View;
import android.widget.Button;

import com.catt.mvp.annotations.InjectMultiPresenter;
import com.catt.mvp.delegated.BaseActivityDelegated;


@InjectMultiPresenter(
        values = {
        "com.example.test.FirstPresenter",
        "com.example.test.SecondPresenter",
        "com.example.test.ThirdPresenter"
})
public class MainView extends BaseActivityDelegated<MainActivity>
        implements IMainActivity.IView {

    private IMainActivity.IPresenter iPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        iPresenter = getPresenterInterface(0);
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
        btn_first = (Button) findViewById(R.id.btn_first);
        btn_first.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (iPresenter instanceof IMainActivity.IFirstPresenter){
                            ((IMainActivity.IFirstPresenter) iPresenter).testMethod();
                        }
                    }
                }
        );
        btn_second = (Button) findViewById(R.id.btn_second);
        btn_second.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (iPresenter instanceof IMainActivity.ISecondPresenter){
                            ((IMainActivity.ISecondPresenter) iPresenter).testMethod();
                        }
                    }
                }
        );
        btn_third = (Button) findViewById(R.id.btn_third);
        btn_third.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (iPresenter instanceof IMainActivity.IThirdPresenter){
                            ((IMainActivity.IThirdPresenter) iPresenter).testMethod();
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
