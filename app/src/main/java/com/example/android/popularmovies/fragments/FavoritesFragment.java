package com.example.android.popularmovies.fragments;


import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.interfaces.LoaderFinishedListener;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.network.LoadDataTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {


    private static final String TAG = FavoritesFragment.class.getSimpleName();

    private static final int TASK_LOADER_ID = 0;

    private MovieAdapter mMovieAdapter;

    private final List<Movie> movieLists = new ArrayList<>();

    private TextView noFavFound;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        setRetainInstance(true);

        noFavFound = rootView.findViewById(R.id.not_favorites_found);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.rv_favorites);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(getContext(), movieLists);
        mRecyclerView.setAdapter(mMovieAdapter);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                //Construct the URI for the item to delete
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = MovieContract.TaskEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // Delete a single row of data using a ContentResolver
                getActivity().getContentResolver().delete(uri, null, null);

                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                // Restart the loader to re-query for all tasks after a deletion
                movieLists.clear();
                mMovieAdapter.notifyDataSetChanged();
                getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, new LoadDataTask(getContext(), new LoadData()));

            }
        }).attachToRecyclerView(mRecyclerView);


        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, new LoadDataTask(getContext(), new LoadData()));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        movieLists.clear();
        mMovieAdapter.notifyDataSetChanged();
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, new LoadDataTask(getContext(), new LoadData()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Help!")
                        .setMessage(R.string.help_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create();
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
        }
        return true;
    }

    public class LoadData implements LoaderFinishedListener<Cursor> {

        @Override
        public void onLoadFinished(Cursor cursor) {
            Log.d(TAG, "onLoadFinished");

            Log.d(TAG, cursor.getCount()+"");

            if (cursor.getCount() > 0) {

                noFavFound.setVisibility(View.INVISIBLE);

                //Log.v(TAG, DatabaseUtils.dumpCursorToString(cursor));

                while (cursor.moveToNext()) {
                    // Indices
                    int idIndex = cursor.getColumnIndex(MovieContract.TaskEntry._ID);
                    int movieIdIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_ID);
                    int voteCountIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_VOTE_COUNT);
                    int titleIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_TITLE);
                    int posterPathIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_POSTER_PATH);
                    int backdropIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_BACKDROP);
                    int releaseDateIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_RELEASE_DATE);
                    int overviewIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_OVERVIEW);
                    int originalLanguageIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE);
                    int popularityIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_POPULARITY);
                    int voteAverageIndex = cursor.getColumnIndex(MovieContract.TaskEntry.COLUMN_MOVIE_VOTE_AVERAGE);


                    // Determine the values of the wanted data

                    int id = cursor.getInt(idIndex);
                    int movie_id = cursor.getInt(movieIdIndex);
                    int vote_count = cursor.getInt(voteCountIndex);
                    String title = cursor.getString(titleIndex);
                    String poster_path = cursor.getString(posterPathIndex);
                    String backdrop_path = cursor.getString(backdropIndex);
                    String release_date = cursor.getString(releaseDateIndex);
                    String overview = cursor.getString(overviewIndex);
                    String original_language = cursor.getString(originalLanguageIndex);
                    double popularity = cursor.getDouble(popularityIndex);
                    double vote_average = cursor.getDouble(voteAverageIndex);

                    Movie movie = new Movie(movie_id, vote_count, title, poster_path, backdrop_path, release_date, overview, original_language, popularity, vote_average);

                    movieLists.add(movie);
                }
                mMovieAdapter.notifyDataSetChanged();
            } else {
                noFavFound.setVisibility(View.VISIBLE);
            }
        }
    }

}
