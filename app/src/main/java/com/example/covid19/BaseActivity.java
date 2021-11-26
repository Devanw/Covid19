package com.example.covid19;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.plugin.sessionmanager.SessionManagerUtil;
import com.example.covid19.view.AuthActivity;

import java.util.Calendar;

public class BaseActivity extends AppCompatActivity {

//    @Override
//    protected void onResume() {
//        boolean isAllowed = SessionManagerUtil.getInstance().isSessionActive(this, Calendar.getInstance().getTime());
//        if (!isAllowed) {
//            Intent intent = new Intent(this, AuthActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//        super.onResume();
//    }

    protected void showMessage (String msg, DialogInterface.OnClickListener onClick) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", onClick)
                .show();
    }

    protected void showMessage (String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
