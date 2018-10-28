package com.example.rus.exercise2_2.network;

import com.example.rus.exercise2_2.network.dto.Result;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NYEndpoint {
    @GET("/{section}.json")
    Call<Result> getNews(@Path("section") String section,
                         @Query("api-key") String apiKey);

    @GET("/{section}.json")
    Single<Result> getNewsRx(@Path("section") String section,
                             @Query("api-key") String apiKey);
}
