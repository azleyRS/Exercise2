package com.example.rus.exercise2_2.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("subsection")
    public String subsection;
    @SerializedName("title")
    public String title;
    @SerializedName("abstract")
    public String _abstract;
    @SerializedName("url")
    public String url;
    @SerializedName("multimedia")
    public List<Multimedium> multimedia = null;
}
