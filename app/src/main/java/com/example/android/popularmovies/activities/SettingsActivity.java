package com.example.android.popularmovies.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.example.android.popularmovies.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize shared pref
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);

        CheckBox exitOnTap = (CheckBox) findViewById(R.id.checkBox_on_tap_exit);
        CheckBox playFullScreen = (CheckBox) findViewById(R.id.checkBox_fullscreen);

        exitOnTap.setChecked(sharedPref.getBoolean("EXIT_ON_TAP", true));
        playFullScreen.setChecked(sharedPref.getBoolean("FULLSCREEN", false));

        // handle click on checkbox
        exitOnTap.setOnClickListener(this);
        playFullScreen.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        final boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBox_on_tap_exit:
                editor.putBoolean("EXIT_ON_TAP", checked);
                editor.apply();
                break;
            case R.id.checkBox_fullscreen:
                editor.putBoolean("FULLSCREEN", checked);
                editor.apply();
                break;
        }
    }
}
