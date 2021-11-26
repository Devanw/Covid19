package com.example.covid19.api.endpoint;

import com.example.covid19.model.covid.AllCovidInfo;
import com.example.covid19.model.covid.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CoronaEndpointInterface {

    // Trailing slash is needed
    String BASE_URL = "https://corona.lmao.ninja/v2/";

    @GET("all")
    Call<AllCovidInfo> getAll();

    @GET("countries")
    Call<List<Country>> getCountries();

    @GET("users/{country}")
    Call<Country> getInfoCountry(@Path("country") String country);

}
