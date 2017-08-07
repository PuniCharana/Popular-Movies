package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TrailerData {

    @SerializedName("id")
    private final int id;

    @SerializedName("results")
    private final List<Trailer> results;

    public TrailerData(int id, List<Trailer> results) {
        this.id = id;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
