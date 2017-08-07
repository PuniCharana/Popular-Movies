package com.example.android.popularmovies.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activities.MovieDetailsActivity;
import com.example.android.popularmovies.databinding.FragmentOverviewBinding;
import com.example.android.popularmovies.interfaces.MoviesApiServices;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieDetails;
import com.example.android.popularmovies.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {


    private static final String TAG = OverviewFragment.class.getSimpleName();

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final FragmentOverviewBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_overview, container, false);

        setRetainInstance(true);

        View view = binding.getRoot();

        Movie movie = ((MovieDetailsActivity) getActivity()).getMovie();

        MovieDetails mDetails = new MovieDetails(
                movie.getPoster_path(),
                false,
                movie.getOverview(),
                movie.getRelease_date(),
                movie.getId(),
                movie.getTitle(),
                movie.getOriginal_language(),
                movie.getTitle(),
                movie.getBackdrop_path(),
                movie.getPopularity(),
                movie.getVote_count(),
                false,
                movie.getVote_average(),
                0,
                "N/A"
        );

        binding.setMovie(mDetails);

        int id = movie.getId();

        MoviesApiServices service =  ApiClient.getClient().create(MoviesApiServices.class);

        Call<MovieDetails> call = service.getMovieDetails(id, getString(R.string.movie_db_api_key));

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails movieDetails = response.body();

                if(movieDetails != null) {
                    Log.d(TAG, "Movie title: "+movieDetails.getTitle());
                    // re-bind with new data
                    binding.setMovie(movieDetails);
                } else {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Failed to load data from "+response.raw().request().url().toString());
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

        return view;
    }

}
