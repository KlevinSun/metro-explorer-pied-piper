package com.example.felixlin.metroexplorer.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.felixlin.metroexplorer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.felixlin.metroexplorer.activity.activity.LandmarkDetailActivity;
import com.example.felixlin.metroexplorer.activity.model.Landmark;


public class LandmarkRecyclerViewAdapter extends RecyclerView.Adapter<LandmarkRecyclerViewAdapter.ViewHolder> {

    List<Landmark> mLandmarks;
    Context context;

    public LandmarkRecyclerViewAdapter(Context context, List<Landmark> landmarks){
        this.mLandmarks = landmarks;
        this.context = context;

    }

    private Context getContext(){
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_landmark, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Landmark landmark = mLandmarks.get(position);

        holder.tvTitle.setText(landmark.getTitle());
        holder.tvOverview.setText(landmark.getOverview());

        Picasso.with(getContext())
                .load(landmark.getPosterPath())
                .into(holder.ivLandmarkImage);

    }

    @Override
    public int getItemCount() {
        return mLandmarks.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivLandmarkImage)
        ImageView ivLandmarkImage;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvOverview)
        TextView tvOverview;
        @BindView(R.id.cvMovie)
        CardView cvMovie;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Landmark landmark = mLandmarks.get(getAdapterPosition());

            Intent intent = new Intent(getContext(), LandmarkDetailActivity.class);
            intent.putExtra("LANDMARK", landmark);
            getContext().startActivity(intent);

        }
    }
}
