package com.example.android.popularmovies.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utils.MovieQueryUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Log tag
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mMovieAdapter;

    private final List<Movie> movieLists = new ArrayList<>();

    private TextView noInternetConnection;
    private ProgressBar mProgressBar;

    // toolbar spinner
    private Spinner spinner;

    // Load more
    private int page = 1; // default start from page 1
    private boolean isLoading = true; // will start loading data by dfault
    private int lastVisibleItem, totalItemCount;

    private static final String MOVIES_QUERY_OPTION= "movies_query_option";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        noInternetConnection = (TextView) findViewById(R.id.tv_no_internet_connection);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_lists);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, movieLists);
        mRecyclerView.setAdapter(mMovieAdapter);

        /*
        * Listen for recycler view scroll
        * when reached to last item load next page
        * */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ( dy > 0 ) { // on scroll down
                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                    // continue only if it is not loading already and if last visible item is the last item itself.
                    if ( !isLoading && (lastVisibleItem >= (totalItemCount-1)) ) {

                        Log.d(LOG_TAG, "totalItemCount "+totalItemCount);
                        Log.d(LOG_TAG, "lastVisibleItem "+lastVisibleItem);
                        Log.d(LOG_TAG, "page "+page);

                        page++;
                        loadMoviesData();
                        isLoading = true;
                    }
                }
            }
        });
    }

    /*
    * 1. Checks internet connectivity
    * 2. gather query params
    * 3. execute query task
    * */
    private void loadMoviesData(){

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            /*
            * get dropdown/spinner selected value
            * Original value: Top Rated
            * Edit: top rated
            * Final: top_rated
            * */
            String query_option_value = spinner.getSelectedItem().toString().toLowerCase();
            String query_option = query_option_value.replaceAll(" ", "_");
            Log.d(LOG_TAG, "Query option: "+query_option);

            String page_number = Integer.toString(page);
            Log.d(LOG_TAG, "Page number: "+page_number);

            /*
            * query_option: eg - upcoming/top_rated/popular/now_playing
            * page_number: eg - 1/2/3/4
            * */
            new FetchMoviesTask().execute(query_option, page_number);
        } else {
            noInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show progress spinner
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            String queryOption = params[0];
            String page = params[1];

            URL movieRequestUrl = MovieQueryUtils.buildUrl(queryOption, page);

            String result = null;
            try {
                result = MovieQueryUtils.fetchMovies(movieRequestUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
            isLoading = false;

            if ( s!=null && !s.equals("") ) {
                List<Movie> movies = MovieQueryUtils.extractFeatureFromJson(s);
                if ( movies != null ) {
                    movieLists.addAll(movies);
                    mMovieAdapter.notifyDataSetChanged();
                }
            } else {
                Log.d(LOG_TAG, "No data found");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // load previously selected spinner/dropdown index
        SharedPreferences prefs = getSharedPreferences(MOVIES_QUERY_OPTION, MODE_PRIVATE);
        int selected_position = prefs.getInt("query_selected_position", 0);
        // set spinner selected to last selected position
        spinner.setSelection(selected_position);

        // Listen for spinner item change
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Save selected position
                SharedPreferences.Editor editor = getSharedPreferences(MOVIES_QUERY_OPTION, MODE_PRIVATE).edit();
                int query_selected_position = spinner.getSelectedItemPosition();
                editor.putInt("query_selected_position", query_selected_position);
                editor.apply();

                // override so that when ever the spinner value change it will start loading from page 1
                page = 1;

                // clear previous data
                movieLists.clear();
                mMovieAdapter.notifyDataSetChanged();

                // Load new movies base on the selected dropdown/spinner value
                loadMoviesData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }
}
