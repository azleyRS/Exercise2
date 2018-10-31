package com.example.rus.exercise2_2.network;

import com.example.rus.exercise2_2.network.dto.NYResponce;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NYEndpoint {
    @GET("{section}.json")
    Single<NYResponce> getNewsRxWithoutKey(@Path("section") String section);
}
