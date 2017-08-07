package com.example.android.popularmovies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.TabsPagerAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.fragments.CastFragment;
import com.example.android.popularmovies.fragments.OverviewFragment;
import com.example.android.popularmovies.fragments.ReviewsFragment;
import com.example.android.popularmovies.fragments.TrailersFragment;
import com.example.android.popularmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tab_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tabs);
        tabLayout.setupWithViewPager(viewPager);


        movie = getIntent().getParcelableExtra("MOVIE");

        String img = movie.getBackdrop_path();
        String title = movie.getTitle();

        getSupportActionBar().setTitle(title);

        String imagePath = "http://image.tmdb.org/t/p/w780"+img;

        ImageView imageView = (ImageView) findViewById(R.id.tab_header);
        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.cinema)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
    }

    public Movie getMovie(){
        return movie;
    }


    private void setupViewPager(ViewPager viewPager) {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OverviewFragment(), "Overview");
        adapter.addFragment(new TrailersFragment(), "Trailers");
        adapter.addFragment(new CastFragment(), "Cast");
        adapter.addFragment(new ReviewsFragment(), "Reviews");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MovieDetailsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_favorite:
                addToFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void addToFavorite() {

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();

        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVote_count());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_BACKDROP, movie.getBackdrop_path());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE, movie.getOriginal_language());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
        contentValues.put(MovieContract.TaskEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_average());

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MovieContract.TaskEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        if(uri != null) {
            Log.d(TAG,  uri.toString());
            Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show();
        }
    }
}
