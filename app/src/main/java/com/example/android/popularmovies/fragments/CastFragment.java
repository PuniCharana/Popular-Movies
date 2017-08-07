package com.example.android.popularmovies.fragments;


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
import com.example.android.popularmovies.activities.MovieDetailsActivity;
import com.example.android.popularmovies.adapters.CastAdapter;
import com.example.android.popularmovies.interfaces.MoviesApiServices;
import com.example.android.popularmovies.models.Cast;
import com.example.android.popularmovies.models.CastData;
import com.example.android.popularmovies.models.Movie;
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
public class CastFragment extends Fragment {


    private static final String TAG = CastFragment.class.getSimpleName();

    private CastAdapter castAdapter;
    private final List<Cast> casts = new ArrayList<>();

    public CastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cast, container, false);

        setRetainInstance(true);

        final TextView notFound = rootView.findViewById(R.id.not_found);
        RecyclerView trailerListView = rootView.findViewById(R.id.cast_list);
        final ProgressBar progressBar = rootView.findViewById(R.id.loading_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        trailerListView.setLayoutManager(linearLayoutManager);

        castAdapter = new CastAdapter(getContext(),  casts);

        trailerListView.setAdapter(castAdapter);

        Movie movie = ((MovieDetailsActivity) getActivity()).getMovie();
        int id = movie.getId();

        InternetConnectivity internetConnectivity = new InternetConnectivity(getContext());

        // If there is a network connection, fetch data
        if (internetConnectivity.isConnected()) {

            // Load only once since we have set setRetainInstance(true)
            if(casts.size() <= 0) {
                MoviesApiServices service =  ApiClient.getClient().create(MoviesApiServices.class);
                Call<CastData> call = service.getMovieCasts(id, getString(R.string.movie_db_api_key));

                call.enqueue(new Callback<CastData>() {
                    @Override
                    public void onResponse(Call<CastData> call, Response<CastData> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(response.isSuccessful()) {
                            CastData castData = response.body();
                            if(castData.getCasts().size() > 0) {
                                casts.addAll(castData.getCasts());
                                castAdapter.notifyDataSetChanged();
                            } else {
                                notFound.setVisibility(View.VISIBLE);
                                Log.d(TAG, "Cast empty");
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Failed to load data from "+response.raw().request().url().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CastData> call, Throwable t) {
                        Log.d(TAG, t.toString());
                    }
                });
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            notFound.setText(getString(R.string.no_internet_connection));
            notFound.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

}
