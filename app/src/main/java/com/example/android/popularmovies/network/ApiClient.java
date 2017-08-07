package com.example.android.popularmovies.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    private static final OkHttpClient httpClient = new OkHttpClient();

    private static final GsonConverterFactory gsonFactory = GsonConverterFactory.create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonFactory)
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}
