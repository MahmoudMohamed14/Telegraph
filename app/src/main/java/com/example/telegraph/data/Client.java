package com.example.telegraph.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public static Retrofit retrofit =null;
    public static Retrofit getClient(String url){
        if(retrofit == null){

            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

   // https://fcm.googleapis.com/

}