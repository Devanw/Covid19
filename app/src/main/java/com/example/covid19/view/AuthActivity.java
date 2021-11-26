package com.example.covid19.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.covid19.MainActivity;
import com.example.covid19.R;
import com.example.covid19.api.endpoint.AuthEndPointInterface;
import com.example.covid19.api.service.AuthService;
import com.example.covid19.api.service.RetroServerCorona;
import com.example.covid19.model.covid.AllCovidInfo;
import com.example.covid19.model.payload.PayloadLogin;
import com.example.covid19.model.user.LoginResponse;
import com.example.covid19.model.user.User;
import com.example.covid19.plugin.retrofit.AuthRetrofit;
import com.example.covid19.plugin.sessionmanager.SessionManagerUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Base64;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    public static final String USERINFO = "USERINFO";
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextView forgotPw;
    private Button btnLogin;
    private ProgressBar pb;

    private String username;
    private String password;

    private AuthService authService;
    private AuthRetrofit authRetrofit;

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate(){

        username = "demo@demo.com";
        password = "demo123";

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
                AuthRetrofit authRetrofit = new AuthRetrofit();
                authRetrofit.getAPI().login(username, password).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        pb.setVisibility(View.INVISIBLE);
                        if (response.body() != null) {
                            mainThread.execute(() -> {
                                Log.e("TAG", "onResponse: " + response.body() );
                                startAndStoreSession(response.body().getData());
                                startMainActivity();
                            });
                        } else {
                            Snackbar.make(findViewById(R.id.loginConstraintLayout), "Error: login failed" , Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
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
    private void startAndStoreSession(User user){
        SessionManagerUtil.getInstance()
                .storeUserToken(this, generateToken(username, password));
        SharedPreferences sharedPreferences = getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String writeData = gson.toJson(user);
        prefsEditor.putString("Userinfo", writeData);
        prefsEditor.commit();
        SessionManagerUtil.getInstance()
                .startUserSession(this, 30);
    }

    private void startMainActivity() {

        RetroServerCorona.getInstance().getAll().enqueue(new Callback<AllCovidInfo>() {
            @Override
            public void onResponse(Call<AllCovidInfo> call, Response<AllCovidInfo> response) {
                try {
                    Log.d("getAll response : ", ""+ response.code() + "\n" + new Gson().toJson(response.body()));
                    Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
                    intent.putExtra(HomeActivity.EXTRA_ALL_COVID_INFO, new Gson().toJson(response.body()));
                    startActivity(intent);
                } catch (Exception e) {
                    showMessage(e.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recreate();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AllCovidInfo> call, Throwable t) {
                Log.e("getAll onFailure : ", t.getMessage());
                showMessage(t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                });
            }
        });
    }

    protected void showMessage (String msg, DialogInterface.OnClickListener onClick) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", onClick)
                .show();
    }

}