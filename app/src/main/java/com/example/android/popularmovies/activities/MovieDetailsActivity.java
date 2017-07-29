package com.example.android.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.popularmovies.R;

public class MovieDetailsActivity extends AppCompatActivity {


    private ImageView mMovieThumbnail;
    private TextView mMovieVoteAverage;
    private TextView mMovieVoteCount;
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieLanguage;
    private TextView mMovieReleaseDate;
    private TextView mMoviePopularity;
    private TextView mMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMovieThumbnail = (ImageView) findViewById(R.id.movie_thumbnail);
        mMovieVoteAverage = (TextView) findViewById(R.id.movie_rating);
        mMovieVoteCount = (TextView) findViewById(R.id.movie_vote_count);
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMovieLanguage = (TextView) findViewById(R.id.movie_language);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mMoviePopularity = (TextView) findViewById(R.id.movie_popularity);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if ( bundle != null ) {
            int id = bundle.getInt("id");
            String title = bundle.getString("title");
            String poster_path = bundle.getString("poster_path");
            String backdrop_path = bundle.getString("backdrop_path");
            String release_date = bundle.getString("release_date");
            String overview = bundle.getString("overview");
            String original_language = bundle.getString("original_language");
            double popularity = bundle.getDouble("popularity");
            double vote_average = bundle.getDouble("vote_average");
            int vote_count = bundle.getInt("vote_count");

            if ( getSupportActionBar() != null ) {
                getSupportActionBar().setTitle(title);
            }

            /*
            * For more image sizes check the link below
            * https://www.themoviedb.org/talk/53c11d4ec3a3684cf4006400?language=en
            * */

            String imageThumbnailPath = "http://image.tmdb.org/t/p/w780"+ backdrop_path;
            Glide.with(this)
                    .load(imageThumbnailPath)
                    .placeholder(R.drawable.cinema)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .centerCrop()
                    .into(mMovieThumbnail);


            mMovieVoteAverage.setText(Double.toString(vote_average));
            mMovieVoteCount.setText(Integer.toString(vote_count));

            String imagePath = "http://image.tmdb.org/t/p/w185"+ poster_path;
            Glide.with(this)
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .into(mMoviePoster);

            mMovieTitle.setText(title);
            mMovieLanguage.setText("Language: "+ original_language);
            mMovieReleaseDate.setText("Release date: "+ release_date);
            mMoviePopularity.setText("Popularity: "+ popularity);
            mMovieOverview.setText(overview);
        }
    }
}
