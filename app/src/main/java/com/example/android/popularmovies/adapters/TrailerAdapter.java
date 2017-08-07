package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Trailer;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final Context context;
    private final List<Trailer> trailerList;

    private static ClickListener clickListener;

    public TrailerAdapter(Context context, List<Trailer> lists) {
        this.context = context;
        this.trailerList = lists;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        final Trailer trailer = trailerList.get(position);
        holder.trailerTitle.setText(trailer.getName());
        holder.trailerSize.setText(Integer.toString(trailer.getSize())+"p");
        holder.trailerSite.setText(trailer.getSite());

        setScaleAnimation(holder.itemView);

    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailerTitle;
        final TextView trailerSize;
        final TextView trailerSite;
        final ImageButton imageButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            trailerTitle = itemView.findViewById(R.id.title);
            trailerSize = itemView.findViewById(R.id.size);
            trailerSite = itemView.findViewById(R.id.site);
            imageButton = itemView.findViewById(R.id.share_btn);

            imageButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TrailerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
