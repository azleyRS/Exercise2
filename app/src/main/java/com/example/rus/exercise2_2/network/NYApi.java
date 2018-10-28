package com.example.rus.exercise2_2.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NYApi {
    private static final String URL = "https://api.nytimes.com/svc/topstories/v2";

    private static NYApi nyApi;
    private final NYEndpoint nyEndpoint;

    public static synchronized NYApi getInstance(){
        if (nyApi == null){
            nyApi = new NYApi();
        }
        return nyApi;
    }

    private NYApi() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        nyEndpoint = retrofit.create(NYEndpoint.class);
    }

    public NYEndpoint getNyEndpoint(){
        return nyEndpoint;
    }
}
