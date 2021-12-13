package com.example.covid19.plugin.retrofit;

import com.example.covid19.api.endpoint.CoronaEndpointInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CovidRetrofit {
    private CoronaEndpointInterface API;

    public CovidRetrofit(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CoronaEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofit.create(CoronaEndpointInterface.class);
        API = retrofit.create(CoronaEndpointInterface.class);
    }

    public CoronaEndpointInterface getAPI(){
        return API;
    }
}
