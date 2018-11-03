package com.example.rus.exercise2_2.network.dto;

import com.example.rus.exercise2_2.TimesUtil;
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
    @SerializedName("published_date")
    public String publishedDate;
    @SerializedName("multimedia")
    public List<Multimedium> multimedia;
}
