package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;



public class Trailer {

    @SerializedName("id")
    private final String id;
    @SerializedName("key")
    private final String key;
    @SerializedName("name")
    private final String name;
    @SerializedName("site")
    private final String site;
    @SerializedName("size")
    private final int size;
    @SerializedName("type")
    private final String type;

    public Trailer(String id, String key, String name, String site, int size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
