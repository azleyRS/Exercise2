package com.example.rus.exercise2_2.network.dto;

import com.google.gson.annotations.SerializedName;

public class Multimedium {
    @SerializedName("url")
    public String url;
    @SerializedName("format")
    public String format;
    @SerializedName("height")
    public Integer height;
    @SerializedName("width")
    public Integer width;
    @SerializedName("type")
    public String type;
    @SerializedName("subtype")
    public String subtype;
    @SerializedName("caption")
    public String caption;
    @SerializedName("copyright")
    public String copyright;
}
