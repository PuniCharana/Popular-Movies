package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;


public class Cast {

    @SerializedName("id")
    private final int id;
    @SerializedName("character")
    private final String character;
    @SerializedName("name")
    private final String name;
    @SerializedName("profile_path")
    private final String profile_path;

    public Cast(int id, String character, String name, String profile_path) {
        this.id = id;
        this.character = character;
        this.name = name;
        this.profile_path = profile_path;
    }

    public int getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }
}
