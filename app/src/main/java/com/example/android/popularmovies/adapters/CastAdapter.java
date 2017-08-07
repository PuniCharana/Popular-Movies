package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activities.ActorDetailsActivity;
import com.example.android.popularmovies.models.Cast;

import java.util.List;



public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder>{

    private final Context context;
    private final List<Cast> castList;
    public CastAdapter(Context context, List<Cast> lists) {
        this.context = context;
        this.castList = lists;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false );
        return new CastAdapter.CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {


        Cast cast = castList.get(position);


        holder.actorName.setText(cast.getName());
        holder.actorCharacterName.setText(cast.getCharacter());

        String imagePath = "http://image.tmdb.org/t/p/w185"+cast.getProfile_path();

        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.person)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .into(holder.profile);
        // Set animation
        setScaleAnimation(holder.itemView);

    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder{

        final ImageView profile;
        final TextView actorName;
        final TextView actorCharacterName;

        public CastViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.actor_profile);
            actorName = itemView.findViewById(R.id.actor_name);
            actorCharacterName = itemView.findViewById(R.id.actor_character);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActorDetailsActivity.class);
                    intent.putExtra("ACTOR_ID", castList.get(getAdapterPosition()).getId());
                    intent.putExtra("ACTOR_NAME", castList.get(getAdapterPosition()).getName());
                    intent.putExtra("ACTOR_PROFILE", castList.get(getAdapterPosition()).getProfile_path());
                    context.startActivity(intent);
                }
            });
        }
    }
}