package com.example.covid19;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.api.service.RetroServerCorona;
import com.example.covid19.model.covid.AllCovidInfo;
import com.example.covid19.view.AuthActivity;
import com.example.covid19.view.HomeActivity;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        RetroServerCorona.getInstance().getAll().enqueue(new Callback<AllCovidInfo>() {
            @Override
            public void onResponse(Call<AllCovidInfo> call, Response<AllCovidInfo> response) {
                try {
                    Log.d("getAll response : ", ""+ response.code() + "\n" + new Gson().toJson(response.body()));
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
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


//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(new Intent(MainActivity.this, HomeActivity.class));
//            }
//        }, 1000);
    }
}