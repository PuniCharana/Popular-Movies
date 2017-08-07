package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class Actor {

    @SerializedName("id")
    private final int id;

    @SerializedName("birthday")
    private final String birthday;

    @SerializedName("deathday")
    private final String deathday;

    @SerializedName("name")
    private final String name;

    @SerializedName("homepage")
    private final String homepage;

    @SerializedName("biography")
    private final String biography;

    @SerializedName("place_of_birth")
    private final String place_of_birth;

    @SerializedName("profile_path")
    private final String profile_path;

    @SerializedName("gender")
    private final String gender;

    public Actor(int id, String birthday, String deathday, String name, String homepage, String biography, String place_of_birth, String profile_path, String gender) {
        this.id = id;
        this.birthday = birthday;
        this.deathday = deathday;
        this.name = name;
        this.homepage = homepage;
        this.biography = biography;
        this.place_of_birth = place_of_birth;
        this.profile_path = profile_path;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getName() {
        return name;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getBiography() {
        return biography;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getGender() {
        return gender;
    }
}
