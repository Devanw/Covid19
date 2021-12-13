package com.example.covid19.api.service;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.covid19.api.endpoint.AuthEndPointInterface;
import com.example.covid19.model.payload.PayloadLogin;
import com.example.covid19.model.user.User;
import com.example.covid19.plugin.retrofit.AuthRetrofit;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AuthService extends AndroidViewModel {
    private AuthEndPointInterface API;
    private User allUsers;

    private final ExecutorService networkExecutor =
            Executors.newFixedThreadPool(4);
    private final Executor mainThread = new Executor() {
        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    public AuthService(@NonNull Application application){
        super(application);
        AuthRetrofit authRetrofit = new AuthRetrofit();
        API = authRetrofit.getAPI();
    }

//    public void login(PayloadLogin payload) {
////        networkExecutor.execute(new Runnable() {
////            @Override
////            public void run() {
////                try {
//////                    User user =
//////                    API.login(payload).enqueue();
//////                    mainThread.execute(new Runnable() {
//////                        @Override
//////                        public void run() {
//////                            allUsers = user;
//////                            Log.e("TAG", "run: " + user );
//////                        }
//////                    });
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
//        });
//    }

//    private MutableLiveData<List<User>> allUsers = new MutableLiveData<>();

//    private final ExecutorService networkExecutor =
//            Executors.newFixedThreadPool(4);
//    private final Executor mainThread = new Executor() {
//        private Handler handler = new Handler(Looper.getMainLooper());
//        @Override
//        public void execute(Runnable command) {
//            handler.post(command);
//        }
//    };
//    public void getUserFromNetwork(){
//        networkExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    List<User> usersFromNetwork = API.getUsers().execute().body();
//                    mainThread.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            allUsers.setValue(usersFromNetwork);
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    MutableLiveData<List<User>> getAllUsers(){
//        if (allUsers.getValue() == null || allUsers.getValue().isEmpty())
//            getUserFromNetwork();
//        return allUsers;
//    }
//
//

}
