package com.vedangj044.statusview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.vedangj044.statusview.ModelObject.ThumbnailStatusObject;
import com.vedangj044.statusview.R;
import com.vedangj044.statusview.Activity.StatusDisplay;
import com.vedangj044.statusview.SettingStatus.SettingStatus;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {

    public List<ThumbnailStatusObject> mDataset = new ArrayList<>();
    // mdataset holds the data to be displayed in the recycler view

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView thumbnailImage;
        // main image of the thumbnail

        private TextView sharedBy, numberOfStatus;
        // name of the person who shared the resource, and no of status he shared

        private MaterialCardView cardView;
        // card view containing all other components

        private ImageView openSettings;

        private Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            sharedBy = itemView.findViewById(R.id.shared_by);
            numberOfStatus = itemView.findViewById(R.id.number_of_status);
            cardView = itemView.findViewById(R.id.card);
            openSettings = itemView.findViewById(R.id.open_setting_activity);
            context = itemView.getContext();
        }
    }

    public StatusAdapter(List<ThumbnailStatusObject> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_thumbnail_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        ThumbnailStatusObject object = mDataset.get(position);

        holder.sharedBy.setText(object.getSharedByName());
        holder.numberOfStatus.setText(String.valueOf(object.getNumberOfStatus()));

        // used glide to automatically load image from url and cache it
        Glide.with(holder.context).load(object.getImageUrl()).into(holder.thumbnailImage);

        // On click listener for each element of the recycler view
        // gives a intent to the status_display activity
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, StatusDisplay.class);
                holder.context.startActivity(intent);
            }
        });

        holder.openSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, SettingStatus.class);
                holder.context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
