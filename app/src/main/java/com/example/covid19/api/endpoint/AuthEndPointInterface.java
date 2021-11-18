package com.example.covid19.api.endpoint;

import com.example.covid19.model.payload.PayloadLogin;
import com.example.covid19.model.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface AuthEndPointInterface {

    // Trailing slash is needed
    String BASE_URL = "https://talentpool.oneindonesia.id/api";

    @POST("/user/login")
    @Headers("X-API-KEY : 454041184B0240FBA3AACD15A1F7A8BB")
    Call<User> login(@Body PayloadLogin login);

}