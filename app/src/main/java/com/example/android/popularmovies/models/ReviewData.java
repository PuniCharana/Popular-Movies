package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ReviewData {

    @SerializedName("id")
    private final int id;

    @SerializedName("page")
    private final int page;

    @SerializedName("totalPages")
    private final int total_pages;

    @SerializedName("totalResults")
    private final int total_results;

    @SerializedName("results")
    private final List<Review> results;

    public ReviewData(int id, int page, int total_pages, int total_results, List<Review> reviews) {
        this.id = id;
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.results = reviews;
    }

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<Review> getResults() {
        return results;
    }
}
