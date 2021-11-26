package com.example.covid19.api.service;

import com.example.covid19.api.endpoint.CoronaEndpointInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServerCorona {

    public static final String base_url = "https://corona.lmao.ninja/v2/";

    private static Retrofit setInit(){
        return new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static CoronaEndpointInterface getInstance() {
        return setInit().create(CoronaEndpointInterface.class);
    }
}
