package com.example.android.popularmovies.network;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.interfaces.LoaderFinishedListener;


public class LoadDataTask implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = LoadDataTask.class.getSimpleName();

    private final Context context;
    private final LoaderFinishedListener<Cursor> loaderFinishedListener;

    public LoadDataTask(Context context, LoaderFinishedListener<Cursor> loaderFinishedListener) {
        this.context = context;
        this.loaderFinishedListener = loaderFinishedListener;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new AsyncTaskLoader<Cursor>(context) {

            @Override
            protected void onStartLoading() {
                // Force a new load
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return context.getContentResolver().query(MovieContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.TaskEntry._ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        loaderFinishedListener.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }
}
