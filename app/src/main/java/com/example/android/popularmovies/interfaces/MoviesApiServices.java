package com.example.android.popularmovies.interfaces;

import com.example.android.popularmovies.models.Actor;
import com.example.android.popularmovies.models.CastData;
import com.example.android.popularmovies.models.MovieDetails;
import com.example.android.popularmovies.models.MoviesByCategory;
import com.example.android.popularmovies.models.ReviewData;
import com.example.android.popularmovies.models.TrailerData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface MoviesApiServices {

    @GET("movie/{category}")
    Call<MoviesByCategory> getMovieByCategory(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/videos")
    Call<TrailerData> getMovieTrailers(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/casts")
    Call<CastData> getMovieCasts(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/reviews")
    Call<ReviewData> getMovieReviews(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    @GET("person/{id}")
    Call<Actor> getActorDetailsById(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );
}

