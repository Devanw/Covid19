package com.example.covid19.plugin.sessionmanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.covid19.component.recyclerview.CountryAdapter;
import com.example.covid19.view.AuthActivity;

import java.util.Calendar;
import java.util.Date;

public class SessionManagerUtil {

    public static final String SESSION_PREFERENCE = "com.example.covid19.SessionManagerUtil.SESSION_PREFERENCE";
    public static final String SESSION_TOKEN = "com.example.covid19.SessionManagerUtil.SESSION_TOKEN";
    public static final String SESSION_EXPIRY_TIME = "com.example.covid19.SessionManagerUtil.SESSION_EXPIRY_TIME";

    private static SessionManagerUtil INSTANCE;
    public static SessionManagerUtil getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SessionManagerUtil();
        }
        return INSTANCE;
    }

    public void startUserSession(Context context, int expiredIn){
        Calendar calendar = Calendar.getInstance();
        Date userLoggedTime = calendar.getTime();
        calendar.setTime(userLoggedTime);
        calendar.add(Calendar.SECOND, expiredIn);
        Date expiryTime = calendar.getTime();
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SESSION_EXPIRY_TIME, expiryTime.getTime());
        editor.apply();
    }

    public boolean isSessionActive(Context context, Date currentTime){
        Date sessionExpiresAt = new Date(getExpiryDateFromPreference(context));
        return !currentTime.after(sessionExpiresAt);
    }

    private long getExpiryDateFromPreference(Context context){
        return context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
                .getLong(SESSION_EXPIRY_TIME, 0);
    }

    public void storeUserToken(Context context, String token){
        SharedPreferences.Editor editor =
                context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString(SESSION_TOKEN, token);
        editor.apply();
    }

    public String getUserToken(Context context){
        return context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
                .getString(SESSION_TOKEN, "");
    }

    public void endUserSession(Context context){
        clearStoredData(context);
    }

    private void clearStoredData(Context context){
        SharedPreferences.Editor bookmark =
                context.getSharedPreferences(CountryAdapter.SAVE, Context.MODE_PRIVATE).edit();
        bookmark.clear();
        bookmark.apply();

        SharedPreferences.Editor userinfo =
                context.getSharedPreferences(AuthActivity.USERINFO, Context.MODE_PRIVATE).edit();
        userinfo.clear();
        userinfo.apply();

        SharedPreferences.Editor editor =
                context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

}
