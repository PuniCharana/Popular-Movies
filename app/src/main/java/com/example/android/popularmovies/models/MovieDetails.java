package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieDetails {

    @SerializedName("poster_path")
    private final String posterPath;
    @SerializedName("adult")
    private final boolean adult;
    @SerializedName("overview")
    private final String overview;
    @SerializedName("release_date")
    private final String releaseDate;
    @SerializedName("genre_ids")
    private final List<Integer> genreIds = new ArrayList<>();
    @SerializedName("id")
    private final Integer id;
    @SerializedName("original_title")
    private final String originalTitle;
    @SerializedName("original_language")
    private final String originalLanguage;
    @SerializedName("title")
    private final String title;
    @SerializedName("backdrop_path")
    private final String backdropPath;
    @SerializedName("popularity")
    private final Double popularity;
    @SerializedName("vote_count")
    private final Integer voteCount;
    @SerializedName("video")
    private final Boolean video;
    @SerializedName("vote_average")
    private final Double voteAverage;
    @SerializedName("runtime")
    private final int runTime;
    @SerializedName("status")
    private final String status;

    public MovieDetails(String posterPath, boolean adult, String overview, String releaseDate, Integer id, String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity, Integer voteCount, Boolean video, Double voteAverage, int runTime, String status) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.runTime = runTime;
        this.status = status;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public int getRunTime() {
        return runTime;
    }

    public String getStatus() {
        return status;
    }
}
