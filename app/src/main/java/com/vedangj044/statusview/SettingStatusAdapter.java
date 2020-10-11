package com.vedangj044.statusview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingStatusAdapter extends RecyclerView.Adapter<SettingStatusAdapter.ViewHolder> {

    private List<SettingStatusModel> mDataset = new ArrayList<>();

    public SettingStatusAdapter(List<SettingStatusModel> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_status_list_obj, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SettingStatusModel object = mDataset.get(position);

        byte[] decodedString = Base64.decode(object.getThumbnail(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.statusThumbnail.setImageBitmap(decodedByte);

        holder.statusViewCount.setText(String.valueOf(object.getViewCount()) + " views");
        holder.statusUpdateTime.setText(object.getUpdateTime());

        holder.statusMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(holder.context, holder.statusMenu, Gravity.CENTER);
                popupMenu.inflate(R.menu.status_setting_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_option_setting:
                                Toast.makeText(holder.context, "Delete option", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView statusThumbnail;
        private TextView statusViewCount;
        private TextView statusUpdateTime;
        private ImageView statusMenu;

        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusThumbnail = itemView.findViewById(R.id.status_thumb);
            statusViewCount = itemView.findViewById(R.id.status_view_count);
            statusUpdateTime = itemView.findViewById(R.id.status_update_time);
            statusMenu = itemView.findViewById(R.id.setting_menu);

            context = itemView.getContext();

        }
    }
}
