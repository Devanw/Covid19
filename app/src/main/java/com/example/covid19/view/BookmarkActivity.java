package com.example.covid19.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.covid19.BaseActivity;
import com.example.covid19.R;

public class BookmarkActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);
    }   
}
