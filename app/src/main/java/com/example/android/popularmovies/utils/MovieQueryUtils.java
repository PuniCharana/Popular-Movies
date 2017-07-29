package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MovieQueryUtils {

    private static final String LOG_TAG = MovieQueryUtils.class.getSimpleName();

    // http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    // API key from https://www.themoviedb.org
    private final static String API_KEY = "YOUR_API_KEY";
    private final static String API_PARAM = "api_key";
    private final static String PAGE_PARAM = "page";

    private MovieQueryUtils(){
        // This class will hold static variables and methods only
    }

    /*
    * @param {String} query: path/source to query
    * @param {String} page: query page number
    * @return {URL} : returns the final query url
    * */
    public static URL buildUrl(String query, String page) {

        Uri builtUri;

        builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /*
    * @param {URL} requestUrl : the url to load movies data
    * @return {String}: return all movies object in string format
    * */
    public static String fetchMovies(URL requestUrl) throws IOException {

        // Perform HTTP request to the URL

        Log.d(LOG_TAG, requestUrl.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /*
    * Extract movie features from given string
    *
    * @param {String} moviesJSON: strings of all movies object
    * @return {List<Movie>} : list of movies in the format of List<Movie>
    * */
    public static List<Movie> extractFeatureFromJson(String moviesJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movie to
        List<Movie> movieLists = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(moviesJSON);


            JSONArray moviesArray = jsonObject.getJSONArray("results");

            // For each movie in the moviesArray, create an  Movie object
            for (int i = 0; i < moviesArray.length(); i++) {

                // Get a single movie at position i within the list of movies
                JSONObject currentMovie = moviesArray.getJSONObject(i);

                // Extract movie data/information
                int id = currentMovie.getInt("id");
                int vote_count = currentMovie.getInt("vote_count");
                String title = currentMovie.getString("title");
                String poster_path = currentMovie.getString("poster_path");
                String backdrop_path = currentMovie.getString("backdrop_path");
                String release_date = currentMovie.getString("release_date");
                String overview = currentMovie.getString("overview");
                String original_language = currentMovie.getString("original_language");
                double popularity = currentMovie.getDouble("popularity");
                double vote_average = currentMovie.getDouble("vote_average");

                // Create new movie object
                Movie movie = new Movie(id, vote_count, title, poster_path, backdrop_path, release_date, overview, original_language, popularity, vote_average);

                // add new movie to list
                movieLists.add(movie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movie JSON results", e);
        }

        // return the final List of movies
        return movieLists;
    }


}
