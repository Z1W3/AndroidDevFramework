package com.example.test.first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.BaseFragment;
import com.example.test.MyPresenter;
import com.example.test.MyPresenterAPI;
import com.example.test.R;

import z1w3.mvp.support.annotations.InjectPresenter;

@InjectPresenter(values = MyPresenter.class)
public class FirstFragment extends BaseFragment implements FirstViewAPI {

    private TextView textView;
    private Button fragmentBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.text_view);
        fragmentBtn = view.findViewById(R.id.fragment_btn);
        fragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenterAPI(MyPresenterAPI.class).fetchText();
            }
        });
    }

    @Override
    public void setText(String content) {
        textView.setText(content);
    }
}
