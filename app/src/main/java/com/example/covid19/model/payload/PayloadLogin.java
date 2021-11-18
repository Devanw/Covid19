package com.example.covid19.model.payload;

public class PayloadLogin {
    private String username;
    private String password;

    public PayloadLogin(String username, String password){
        this.username = username;
        this.password = password;
    }
}
