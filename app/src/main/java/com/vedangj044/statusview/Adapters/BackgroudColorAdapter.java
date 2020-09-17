package com.vedangj044.statusview.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

// Adapter to populate the background chossing fragment with image views;
public class BackgroudColorAdapter extends BaseAdapter {

    // context and backgroundResouce list contains the string of color code;
    private Context mContext;
    private List<Integer> backgroundResource = new ArrayList<>();

    // position of the item that needs to be marked selected
    private int current;

    // Default height of each cell
    private static int height = 70;

    public BackgroudColorAdapter(Context mContext, List<Integer> backgroudResource, int current) {
        this.mContext = mContext;
        this.backgroundResource = backgroudResource;
        this.current = current;
    }

    @Override
    public int getCount() {
        return backgroundResource.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout relativeLayout;

        if(convertView == null){
            relativeLayout = new RelativeLayout(mContext);
        }
        else{
            relativeLayout = (RelativeLayout) convertView;
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height*Resources.getSystem().getDisplayMetrics().density));
        relativeLayout.setLayoutParams(layoutParams);


        ImageView imageResource = new ImageView(mContext);
        imageResource.setLayoutParams(layoutParams);

        imageResource.setImageResource(backgroundResource.get(position));

        relativeLayout.setId(position);

        ImageView imageTick = new ImageView(mContext);
        imageTick.setLayoutParams(layoutParams);
        imageTick.setId(100+position);
        imageTick.setElevation(50);


        if(current == position){
            imageTick.setImageResource(R.drawable.ic_tick_foreground);
        }
        else{
            imageTick.setImageResource(0);
        }

        relativeLayout.addView(imageResource);
        relativeLayout.addView(imageTick);

        return relativeLayout;
    }

}
