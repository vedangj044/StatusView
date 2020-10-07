package com.vanniktech.emoji_stickers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.viewpager.widget.PagerAdapter;

import com.vanniktech.emoji.listeners.OnEmojiClickListener;
import com.vanniktech.emoji.listeners.OnEmojiLongClickListener;
import com.vanniktech.emoji_stickers.stickers.Sticker;
import com.vanniktech.emoji_stickers.stickers.StickerListener;

import java.util.List;

public final class StickerPagerAdapter extends PagerAdapter {
    private static final int RECENT_POSITION = 0;

    private Context mContext;
    private List<List<String>> urlsList;
    private StickerListener stickerListener;

    public StickerPagerAdapter(Context context, List<List<String>> urlsList) {
        this.mContext = context;
        this.urlsList = urlsList;
    }

    public void setStickerListener(StickerListener stickerListener) {
        this.stickerListener = stickerListener;
    }

    @Override public int getCount() {
        return urlsList.size();
    }

    @Override public Object instantiateItem(final ViewGroup pager, final int position) {
        final GridView newView;

        newView = new GridView(mContext);
        newView.setAdapter(new StickerArrayAdapter(mContext, urlsList.get(position)));
        newView.setNumColumns(4);
        newView.setVerticalSpacing(100);
        newView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                if(stickerListener != null){
                    stickerListener.onClick(new Sticker(urlsList.get(position).get(position1)));
                }
            }
        });

        pager.addView(newView);
        return newView;
    }

    @Override public void destroyItem(final ViewGroup pager, final int position, final Object view) {
        pager.removeView((View) view);
    }

    @Override public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }
}