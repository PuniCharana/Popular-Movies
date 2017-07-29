package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activities.MovieDetailsActivity;
import com.example.android.popularmovies.models.Movie;

import java.util.List;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private final Context context;
    private final List<Movie> movieList;

    public MovieAdapter(Context c, List<Movie> movies) {
        this.context = c;
        this.movieList = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_single_item, parent, false );
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final Movie movie = movieList.get(position);

        String imagePath = "http://image.tmdb.org/t/p/w185"+movie.getPoster_path();

        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.cinema)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("id", movie.getId());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("poster_path", movie.getPoster_path());
                intent.putExtra("backdrop_path", movie.getBackdrop_path());
                intent.putExtra("release_date", movie.getRelease_date());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("original_language", movie.getOriginal_language());
                intent.putExtra("popularity", movie.getPopularity());
                intent.putExtra("vote_average", movie.getVote_average());
                intent.putExtra("vote_count", movie.getVote_count());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        private final ImageView thumbnail;

        public MovieViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.iv_movie_thumbnail);
        }
    }
}
