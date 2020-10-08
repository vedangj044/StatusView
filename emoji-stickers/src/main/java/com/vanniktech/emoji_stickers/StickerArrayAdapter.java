package com.vanniktech.emoji_stickers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class StickerArrayAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> stickerUrl = new ArrayList<>();

    public StickerArrayAdapter(@NonNull final Context context, @NonNull final List<String> stickerUrl) {
        this.mContext = context;
        this.stickerUrl = stickerUrl;
    }

    @Override
    public int getCount() {
        return stickerUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override @NonNull public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        ImageView imageView;

        if(convertView == null){
            imageView = new ImageView(mContext);
        }
        else{
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundColor(Color.TRANSPARENT);

        Glide.with(mContext).load(stickerUrl.get(position)).into(imageView);
        return imageView;
    }

}
