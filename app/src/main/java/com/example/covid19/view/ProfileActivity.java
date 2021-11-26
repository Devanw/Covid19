package com.example.covid19.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.covid19.BaseActivity;
import com.example.covid19.R;
import com.example.covid19.model.user.User;
import com.example.covid19.plugin.sessionmanager.SessionManagerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ProfileActivity extends BaseActivity {

    private Button logoutBtn;

    private TextView username;
    private TextView email;
    private TextView fullname;
    private TextView website;
    private TextView phone;
    private TextView address;
    private TextView company;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.provile_activity);
        SharedPreferences userSp = getSharedPreferences(AuthActivity.USERINFO, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userSp.getString("Userinfo", null);
        Type type = new TypeToken<User>() {
        }.getType();
        User userFromSp = gson.fromJson(json, type);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setElevation(0);


        username = findViewById(R.id.pUsernameValue);
        email = findViewById(R.id.pEmailValue);
        fullname = findViewById(R.id.pFullnameValue);
        website = findViewById(R.id.pWebsiteValue);
        phone = findViewById(R.id.pPhoneValue);
        address = findViewById(R.id.pAddressValue);
        company = findViewById(R.id.pCompanyValue);

        if (userFromSp !=  null){
            username.setText(userFromSp.getUsername());
            email.setText(userFromSp.getEmail());
            fullname.setText(userFromSp.getFullName());
        }
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            SessionManagerUtil.getInstance().endUserSession(v.getContext());
            Intent intent = new Intent(v.getContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
