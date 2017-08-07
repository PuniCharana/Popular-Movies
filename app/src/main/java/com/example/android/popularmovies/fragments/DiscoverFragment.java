package com.example.android.popularmovies.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.interfaces.MoviesApiServices;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MoviesByCategory;
import com.example.android.popularmovies.network.ApiClient;
import com.example.android.popularmovies.utils.InternetConnectivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {


    private static final String TAG = DiscoverFragment.class.getSimpleName();
    // toolbar spinner
    private Spinner spinner;

    private static final String MOVIES_QUERY_OPTION= "movies_query_option";


    private MovieAdapter mMovieAdapter;

    private final List<Movie> movieLists = new ArrayList<>();

    private TextView noInternetConnection;
    private ProgressBar mProgressBar;


    // Load more
    private int page = 1; // default start from page 1
    private boolean isLoading = true; // will start loading data by dfault
    private int lastVisibleItem, totalItemCount;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movies, container, false);

        setRetainInstance(true);

        noInternetConnection = rootView.findViewById(R.id.tv_no_internet_connection);
        mProgressBar = rootView.findViewById(R.id.pb_loading_indicator);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.rv_movies_lists);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(getContext(), movieLists);
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

                        Log.d(TAG, "totalItemCount "+totalItemCount);
                        Log.d(TAG, "lastVisibleItem "+lastVisibleItem);
                        Log.d(TAG, "page "+page);

                        page++;
                        loadMoviesData();
                        isLoading = true;
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.discover_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // load previously selected spinner/dropdown index
        SharedPreferences prefs = getContext().getSharedPreferences(MOVIES_QUERY_OPTION, Context.MODE_PRIVATE);
        final int selected_position = prefs.getInt("query_selected_position", 0);
        // set spinner selected to last selected position
        spinner.setSelection(selected_position);

        // Listen for spinner item change
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Save selected position
                SharedPreferences.Editor editor = getContext().getSharedPreferences(MOVIES_QUERY_OPTION, Context.MODE_PRIVATE).edit();
                int query_selected_position = spinner.getSelectedItemPosition();
                editor.putInt("query_selected_position", query_selected_position);
                editor.apply();

                /*
                * NOTE: (spinner) OnItemSelectedListener is called whenever the fragment is visible to user
                *
                *  But we don't want to called the api (loadMoviesData()) every time.
                *  In case of:
                *  1. screen rotation (setRetainInstance(true))
                *  2. coming back from favorite tab (setRetainInstance(true))
                *  3. returning from other activity (launchMode="singleTop")
                *
                *  Call the api only if
                *  a. first time (movieLists.size() <= 0)
                *  b. spinner value changed (selected_position == query_selected_position)
                *
                *  NOTE: when loadMoviesData() is called old data is cleared and replaced with fresh data.
                * */

                // spinner not changed
                if (selected_position == query_selected_position) {
                    if (movieLists.size() <= 0) { // first time loading
                        // override so that when ever the spinner value change it will start loading from page 1
                        page = 1;
                        loadMoviesData();
                    } else {
                        // Do not call the api again
                        // Reason: Spinner value has not change yet and previous movie list items are present.
                    }
                } else { // spinner has changed
                    page = 1;
                    loadMoviesData();
                }

                // Load new movies base on the selected dropdown/spinner value
                // loadMoviesData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    /*
    * 1. Checks internet connectivity
    * 2. gather query params
    * 3. execute query task
    * */
    private void loadMoviesData(){

        mProgressBar.setVisibility(View.VISIBLE);

        InternetConnectivity internetConnectivity = new InternetConnectivity(getContext());

        // If there is a network connection, fetch data
        if (internetConnectivity.isConnected()) {
            noInternetConnection.setVisibility(View.INVISIBLE);
            /*
            * get dropdown/spinner selected value
            * Original value: Top Rated
            * Edit: top rated
            * Final: top_rated
            * */
            String query_option_value = spinner.getSelectedItem().toString().toLowerCase();
            String query_option = query_option_value.replaceAll(" ", "_");
            Log.d(TAG, "Query option: "+query_option);

            Log.d(TAG, "Page number: "+page);


            MoviesApiServices service =  ApiClient.getClient().create(MoviesApiServices.class);
            Call<MoviesByCategory> call = service.getMovieByCategory(query_option, getString(R.string.movie_db_api_key), page);
            call.enqueue(new Callback<MoviesByCategory>() {
                @Override
                public void onResponse(Call<MoviesByCategory> call, Response<MoviesByCategory> response) {

                    mProgressBar.setVisibility(View.INVISIBLE);
                    isLoading = false;

                    if (response.isSuccessful()) {

                        // clear prev data if loading for first time
                        if (page == 1) {
                            // clear previous data
                            movieLists.clear();
                            mMovieAdapter.notifyDataSetChanged();
                        }

                        MoviesByCategory moviesByCategory = response.body();

                        List<Movie> movies = moviesByCategory.getResults();

                        if ( movies != null ) {

                            int start = movieLists.size();

                            movieLists.addAll(movies);

                            int end = movieLists.size();

                            Log.d(TAG, start+" "+end);
                            mMovieAdapter.notifyItemRangeInserted(start, end);
                        }

                    } else {
                        Toast.makeText(getContext(), "No found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MoviesByCategory> call, Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });

        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            noInternetConnection.setVisibility(View.VISIBLE);
        }
    }
}
