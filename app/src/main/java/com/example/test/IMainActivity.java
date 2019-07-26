package com.example.test;

import com.catt.mvp.annotations.DeclaredIPresenter;
import com.catt.mvp.annotations.DeclaredIView;

public interface IMainActivity {

    @DeclaredIView
    interface IView{
        void onChangeText1(String content);
        void onChangeText2(String content);
        void onChangeText3(String content);
    }


    interface IPresenter{}

    @DeclaredIPresenter
    interface IFirstPresenter extends IPresenter{

        void testMethod();
    }

    @DeclaredIPresenter
    interface ISecondPresenter extends IPresenter{

        void testMethod();
    }

    @DeclaredIPresenter
    interface IThirdPresenter extends IPresenter{

        void testMethod();
    }
}
