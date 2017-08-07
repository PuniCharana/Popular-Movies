package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastData {

    @SerializedName("id")
    private final int id;
    @SerializedName("cast")
    private final List<Cast> casts;

    public CastData(int id, List<Cast> casts) {
        this.id = id;
        this.casts = casts;
    }

    public int getId() {
        return id;
    }

    public List<Cast> getCasts() {
        return casts;
    }

}
