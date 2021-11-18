package com.example.covid19.api.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.covid19.api.endpoint.AuthEndPointInterface;
import com.example.covid19.plugin.retrofit.AuthRetrofit;


public class AuthService extends AndroidViewModel {
    private AuthEndPointInterface API;
    private AuthRetrofit authRetrofit;

    public AuthService(@NonNull Application application){
        super(application);
        AuthRetrofit authRetrofit = new AuthRetrofit();
        API = authRetrofit.getAPI();
    }

    public AuthRetrofit getAuthRetrofit() {
        return authRetrofit;
    }

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
