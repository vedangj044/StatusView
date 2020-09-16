package com.vedangj044.statusview.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.Arrays;
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

        imageResource.setBackgroundResource(backgroundResource.get(position));

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

//        final ImageView Item;
//
//        if(convertView == null){
//            Item = new ImageView(mContext);
//        }
//        else{
//            Item = (ImageView) convertView;
//        }
//
//        // Linear layout params to set 70dp height
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height*Resources.getSystem().getDisplayMetrics().density));
//        Item.setLayoutParams(layoutParams);
//        Item.setId(position);
//
//        if(current == position){
//
//            if(isColor.get(position)){
//                // This position is selected
//                Item.setBackgroundResource(R.drawable.round_white);
//
//                LayerDrawable drawable = (LayerDrawable) Item.getBackground();
//                drawable.setColorFilter(Color.parseColor(backgroundResource.get(position)), PorterDuff.Mode.DST_OVER);
//            }
//            else{
//                Item.setBackgroundResource(Integer.parseInt(backgroundResource.get(position)));
//
////                LayerDrawable drawable = (LayerDrawable) Item.getBackground();
////                drawable.setColorFilter(Color.parseColor(backgroundResource.get(position)), PorterDuff.Mode.DST_OVER);
//
//                // do nothing;
//            }
//
//        }
//        else{
//
//            if(isColor.get(position)){
//                // Background resource file
//                Item.setBackgroundResource(R.drawable.custom_rounded_backgroud);
//
//                // To use background resource and background color together we need Gradient drawable
//                // Reference: https://stackoverflow.com/questions/11977302/android-set-background-resource-and-color
//                GradientDrawable drawable = (GradientDrawable) Item.getBackground();
//                drawable.setColor(Color.parseColor(backgroundResource.get(position)));
//            }
//            else{
//                Item.setBackgroundResource(Integer.parseInt(backgroundResource.get(position)));
//            }


//        }



        return relativeLayout;
    }

}
