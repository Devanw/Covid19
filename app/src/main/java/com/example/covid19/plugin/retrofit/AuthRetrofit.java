package com.example.covid19.plugin.retrofit;

import com.example.covid19.api.endpoint.AuthEndPointInterface;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRetrofit {
    private AuthEndPointInterface API;

    public AuthRetrofit(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AuthEndPointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofit.create(AuthEndPointInterface.class);
        API = retrofit.create(AuthEndPointInterface.class);
    }

    public AuthEndPointInterface getAPI(){
        return API;
    }
}
