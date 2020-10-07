package com.vedangj044.statusview.Stickers;

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
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class MyStickerRecyclerAdapter extends RecyclerView.Adapter<MyStickerRecyclerAdapter.ViewHolder> {

    public List<AllStickerModel> mDataset = new ArrayList<>();
    private Context context;
    private StickerDatabase stickerDatabase;

    public MyStickerRecyclerAdapter(Context context) {
        this.context = context;
        this.stickerDatabase = StickerDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_card_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllStickerModel arr = mDataset.get(position);
        holder.deleteIcon.setImageResource(R.drawable.camera_delete_foreground);

        holder.titleTextView.setText(arr.getName());

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context.getApplicationContext(), FullStickerPack.class);
                intent.putStringArrayListExtra("urllist", (ArrayList<String>) arr.getImages());
                intent.putExtra("title", arr.getName());
                holder.context.startActivity(intent);
            }
        });
        
        List<ImageView> imageObject = new ArrayList<>();
        imageObject.add(holder.icon1);
        imageObject.add(holder.icon2);
        imageObject.add(holder.icon3);
        imageObject.add(holder.icon4);
        imageObject.add(holder.icon5);

        for(int i = 0; i < Math.min(5, arr.getImages().size()); i++){
            Glide.with(holder.context).load(arr.getImages().get(i))
                    .into(imageObject.get(i));

        }

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerDatabase.stickerCategoryDAO().deleteStickerCategory(arr);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView;
        private TextView sizeTextView;

        private ImageView icon1;
        private ImageView icon2;
        private ImageView icon3;
        private ImageView icon4;
        private ImageView icon5;

        private ImageView deleteIcon;

        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.sticker_pack_title);
            sizeTextView = itemView.findViewById(R.id.sticker_pack_size);

            icon1 = itemView.findViewById(R.id.sticker_icon_1);
            icon2 = itemView.findViewById(R.id.sticker_icon_2);
            icon3 = itemView.findViewById(R.id.sticker_icon_3);
            icon4 = itemView.findViewById(R.id.sticker_icon_4);
            icon5 = itemView.findViewById(R.id.sticker_icon_5);

            deleteIcon = itemView.findViewById(R.id.sticker_download_state);

            context = itemView.getContext();
        }
    }
}
