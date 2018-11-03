package com.example.rus.exercise2_2.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NYResponce {
    @SerializedName("results")
    public List<Result> results;
}
