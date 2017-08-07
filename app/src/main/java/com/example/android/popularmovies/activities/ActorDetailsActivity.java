package com.example.android.popularmovies.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.example.android.popularmovies.databinding.ActivityActorDetailsBinding;
import com.example.android.popularmovies.interfaces.MoviesApiServices;
import com.example.android.popularmovies.models.Actor;
import com.example.android.popularmovies.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorDetailsActivity extends AppCompatActivity {

    private static final String TAG = ActorDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityActorDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_actor_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView actorProfileImageView = (ImageView) findViewById(R.id.actor_profile);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int actorId;
        String actorName;
        String actorProfile;
        if (bundle != null) {
            actorId = bundle.getInt("ACTOR_ID");
            actorName = bundle.getString("ACTOR_NAME");
            actorProfile = bundle.getString("ACTOR_PROFILE");

            // Set actor name as title
            getSupportActionBar().setTitle(actorName);

            String imagePath = "http://image.tmdb.org/t/p/w300"+actorProfile;

            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.person)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .centerCrop()
                    .into(actorProfileImageView);

            Log.d(TAG, "Actor id: "+actorId);

            MoviesApiServices service =  ApiClient.getClient().create(MoviesApiServices.class);

            Call<Actor> call = service.getActorDetailsById(actorId, getString(R.string.movie_db_api_key));

            call.enqueue(new Callback<Actor>() {
                @Override
                public void onResponse(Call<Actor> call, Response<Actor> response) {
                    Actor actorDetails = response.body();

                    if(actorDetails != null) {

                        binding.setActor(actorDetails);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to load data from "+response.raw().request().url().toString());
                    }
                }

                @Override
                public void onFailure(Call<Actor> call, Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_settings:
                startActivity(new Intent(ActorDetailsActivity.this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
