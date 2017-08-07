package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmovies.data.MovieContract.TaskEntry;


class MovieDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID                               + " INTEGER PRIMARY KEY, " +
                TaskEntry.COLUMN_MOVIE_ID                   + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_MOVIE_VOTE_COUNT           + " INTEGER, " +
                TaskEntry.COLUMN_MOVIE_TITLE                + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_MOVIE_POSTER_PATH          + " TEXT, " +
                TaskEntry.COLUMN_MOVIE_BACKDROP             + " TEXT, " +
                TaskEntry.COLUMN_MOVIE_RELEASE_DATE         + " TEXT, " +
                TaskEntry.COLUMN_MOVIE_OVERVIEW             + " TEXT, " +
                TaskEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE    + " TEXT, " +
                TaskEntry.COLUMN_MOVIE_POPULARITY           + " REAL, " +
                TaskEntry.COLUMN_MOVIE_VOTE_AVERAGE         + " REAL );";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
