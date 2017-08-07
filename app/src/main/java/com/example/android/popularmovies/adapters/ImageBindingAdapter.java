package com.example.android.popularmovies.adapters;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.popularmovies.R;



public class ImageBindingAdapter {

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {

        /*
        * For more image sizes check the link below
        * https://www.themoviedb.org/talk/53c11d4ec3a3684cf4006400?language=en
        * */
        String imageThumbnailPath = "http://image.tmdb.org/t/p/w780"+ url;

        Glide.with(imageView.getContext())
                .load(imageThumbnailPath)
                .placeholder(R.drawable.cinema)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }
}
