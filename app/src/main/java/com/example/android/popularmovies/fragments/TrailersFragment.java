package com.example.android.popularmovies.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activities.CustomYoutubePlayerActivity;
import com.example.android.popularmovies.activities.MovieDetailsActivity;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.interfaces.MoviesApiServices;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.models.TrailerData;
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
public class TrailersFragment extends Fragment {


    private static final String TAG = TrailersFragment.class.getSimpleName();

    private TrailerAdapter trailerAdapter;

    public TrailersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);

        setRetainInstance(true);

        final List<Trailer> trailerList = new ArrayList<>();

        final TextView notFound = rootView.findViewById(R.id.not_found);

        final RecyclerView trailerRecyclerView = rootView.findViewById(R.id.rv_trailers);
        final ProgressBar progressBar = rootView.findViewById(R.id.loading_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);

        trailerAdapter = new TrailerAdapter(getContext(),  trailerList);
        trailerRecyclerView.setAdapter(trailerAdapter);

        final Movie movie = ((MovieDetailsActivity) getActivity()).getMovie();
        int id = movie.getId();

        InternetConnectivity internetConnectivity = new InternetConnectivity(getContext());

        // If there is a network connection, fetch data
        if (internetConnectivity.isConnected()) {
            // Load only once since we have set setRetainInstance(true)
            if(trailerList.size() <= 0) {
                MoviesApiServices service = ApiClient.getClient().create(MoviesApiServices.class);
                Call<TrailerData> call = service.getMovieTrailers(id, getString(R.string.movie_db_api_key));
                call.enqueue(new Callback<TrailerData>() {
                    @Override
                    public void onResponse(Call<TrailerData> call, Response<TrailerData> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.isSuccessful()) {
                            TrailerData trailerData = response.body();

                            if (trailerData.getResults().size() > 0) {
                                trailerList.addAll(trailerData.getResults());
                                trailerAdapter.notifyDataSetChanged();
                            } else {
                                notFound.setVisibility(View.VISIBLE);
                                Log.d(TAG, "Trailer is empty");
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Failed to load data from " + response.raw().request().url().toString());

                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerData> call, Throwable t) {
                        Log.d(TAG, t.toString());
                    }
                });
            }  else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            notFound.setText(getString(R.string.no_internet_connection));
            notFound.setVisibility(View.VISIBLE);
        }


        trailerAdapter.setOnItemClickListener(new TrailerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId() == R.id.share_btn) {
                    String text = "Watch "+movie.getTitle()
                           + " trailer.\n\nhttps://www.youtube.com/watch?v="+trailerList.get(position).getKey();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share Via"));

                } else {

                    List<String> vIds = new ArrayList<>();

                    for (int i=0; i<trailerList.size(); i++) {
                        vIds.add(trailerList.get(i).getKey());
                    }

                    Intent youtubeIntent = new Intent(getContext(), CustomYoutubePlayerActivity.class);
                    Bundle b = new Bundle();
                    b.putStringArrayList("VIDEO_IDS", (ArrayList<String>) vIds);
                    b.putInt("KEY_START_INDEX", position);
                    youtubeIntent.putExtras(b);
                    startActivity(youtubeIntent);
                }

            }
        });

        return rootView;
    }
}
