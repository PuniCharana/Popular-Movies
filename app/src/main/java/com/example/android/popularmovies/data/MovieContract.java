package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";

    private static final Uri CONTENT_BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String CONTENT_PATH = "movies";

    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI = CONTENT_BASE_URI.buildUpon().appendPath(CONTENT_PATH).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop_path";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";

    }
}
