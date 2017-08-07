package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MoviesByCategory {

    @SerializedName("page")
    private final int page;
    @SerializedName("results")
    private final List<Movie> results;
    @SerializedName("total_results")
    private final int totalResults;
    @SerializedName("total_pages")
    private final int totalPages;

    public MoviesByCategory(int page, List<Movie> results, int totalResults, int totalPages) {
        this.page = page;
        this.results = results;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
