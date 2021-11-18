package com.example.covid19.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.BaseActivity;
import com.example.covid19.R;
import com.example.covid19.api.service.AuthService;
import com.example.covid19.model.payload.PayloadLogin;
import com.example.covid19.model.user.User;
import com.example.covid19.plugin.sessionmanager.SessionManagerUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Base64;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextView forgotPw;
    private Button btnLogin;
    private ProgressBar pb;

    private String username;
    private String password;

    private AuthService authService;

    private Executor backgroundThread = Executors.newSingleThreadExecutor();
    private Executor mainThread = new Executor() {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate: auth" );
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.auth_activity);

        pb = findViewById(R.id.progressbarlgn);
        tilUsername = findViewById(R.id.unameInputLayout);
        tilPassword = findViewById(R.id.pwordInputLayout);

        forgotPw = findViewById(R.id.forgotPasswordTV);
        forgotPw.setText(Html.fromHtml("<a href=\"\">" + forgotPw.getText() + "</a>"));
        forgotPw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Feature is currently unavailable",Toast.LENGTH_SHORT).show();
            }
        });


        btnLogin = findViewById(R.id.loginBtn);
//        btnLogin.setBackgroundColor(getResources().getColor(R.color.darkerred));
//        btnLogin.setTextColor(getResources().getColor(R.color.white));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate(){

        username = tilUsername.getEditText().getText().toString();
        password = tilPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Username dan password tidak boleh kosong!!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            login(username, password);
        }


    }

    private void login(String username, String password){
        pb.setVisibility(View.VISIBLE);
        backgroundThread.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                // connect server
                PayloadLogin payloadLogin = new PayloadLogin(username, password);
                authService.getAuthRetrofit().getAPI().login(payloadLogin)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        pb.setVisibility(View.INVISIBLE);
                        mainThread.execute(() -> {
                            startAndStoreSession();
                            startMainActivity();
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        pb.setVisibility(View.INVISIBLE);
                        Snackbar.make(findViewById(R.id.loginConstraintLayout), "Error: " + t.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
                    }
                });
            }
        });
    }


    private String generateToken(String username, String password){
        String feeds = username+":"+password;
        String token = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            token = Base64.getEncoder().encodeToString(feeds.getBytes());
        } else {
            token = feeds;
        }
        return token;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startAndStoreSession(){
        SessionManagerUtil.getInstance()
                .storeUserToken(this, generateToken(username, password));
        SessionManagerUtil.getInstance()
                .startUserSession(this, 30);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
    }
}