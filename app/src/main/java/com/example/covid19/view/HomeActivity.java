package com.example.covid19.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.covid19.BaseActivity;
import com.example.covid19.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("TAG", "onCreate: " );
    }
}
