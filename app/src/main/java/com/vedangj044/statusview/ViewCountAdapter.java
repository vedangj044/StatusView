package com.vedangj044.statusview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewCountAdapter extends RecyclerView.Adapter<ViewCountAdapter.ViewHolder> {

    List<ViewCountObject> mDataset = new ArrayList<>();

    public ViewCountAdapter(List<ViewCountObject> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_count_list_obj, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewCountObject vho = mDataset.get(position);

        holder.name.setText(vho.getName());
        holder.viewTime.setText(vho.getTime());

        Glide.with(holder.context).load(vho.getProfileImageURL()).into(holder.profileIcon);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profileIcon;
        private TextView name;
        private TextView viewTime;

        private Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileIcon = itemView.findViewById(R.id.profileIconViewCount);
            name = itemView.findViewById(R.id.view_count_name);
            viewTime = itemView.findViewById(R.id.view_count_time);

            context = itemView.getContext();

        }
    }
}
