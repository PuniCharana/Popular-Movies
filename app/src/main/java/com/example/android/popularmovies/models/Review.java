package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;



public class Review {

    @SerializedName("id")
    private final String id;
    @SerializedName("author")
    private final String author;
    @SerializedName("content")
    private final String content;
    @SerializedName("url")
    private final String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
