package com.example.android.popularmovies.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private final int id;
    private final int vote_count;
    private final String title;
    private final String poster_path;
    private final String backdrop_path;
    private final String release_date;
    private final String overview;
    private final String original_language;
    private final double popularity;
    private final double vote_average;



    public Movie(int id, int vote_count, String title, String poster_path, String backdrop_path, String release_date, String overview, String original_language, double popularity, double vote_average) {
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.release_date = release_date;
        this.overview = overview;
        this.original_language = original_language;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        vote_count = in.readInt();
        title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        overview = in.readString();
        original_language = in.readString();
        popularity = in.readDouble();
        vote_average = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(vote_count);
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeString(original_language);
        parcel.writeDouble(popularity);
        parcel.writeDouble(vote_average);
    }
}
