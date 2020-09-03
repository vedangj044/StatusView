package com.vedangj044.statusview.BottomSheets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vedangj044.statusview.Adapters.BackgroudColorAdapter;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackgroundBottomSheet extends Fragment {

    // Interface to send messaged back to the activity
    private BackgroundChangeListener listener;

    // Contains the string of color code
    private List<String> BackgroundColorResource = new ArrayList<>();

    // Interface to send message back to the activity
    // Change commit messages
    public interface BackgroundChangeListener{
        // send the color string value
        void onInputASend(String color);

        // shows the keyboard in the createTextStatus activity
        void onCallKeyboardA(int i);
    }

    // variable keeps track of which color is selected and also makes sure not more than
    // one color is selected at the same time
    private int current_Selected = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_background_select, container, false);

        BackgroundColorResource.add("#778899");
        BackgroundColorResource.add("#290001");
        BackgroundColorResource.add("#f8bd7f");
        BackgroundColorResource.add("#4e89ae");
        BackgroundColorResource.add("#206a5d");
        BackgroundColorResource.add("#ffc93c");
        BackgroundColorResource.add("#1a1a2e");
        BackgroundColorResource.add("#2d4059");
        BackgroundColorResource.add("#382933");


        final GridView gridView = v.findViewById(R.id.background_selector);

        // getting argument to keep the selection persistent
        current_Selected = BackgroundColorResource.indexOf(getArguments().getString("background"));

        // set adapter for grid view
        BackgroudColorAdapter bgAdapter = new BackgroudColorAdapter(v.getContext(), BackgroundColorResource, current_Selected);
        gridView.setAdapter(bgAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // when clicked the first time
                if(current_Selected == -1){
                    current_Selected = position;

                    // set background to have a tick
                    view.setBackgroundResource(R.drawable.round_white);
                    // set color using Layer drawable
                    // reference: https://stackoverflow.com/questions/11977302/android-set-background-resource-and-color
                    LayerDrawable drawable = (LayerDrawable) view.getBackground();
                    drawable.setColorFilter(Color.parseColor(BackgroundColorResource.get(current_Selected)), PorterDuff.Mode.DST_OVER);
                }
                else{

                    // Clears the already selected color by restoring default background
                    gridView.getChildAt(current_Selected).setBackgroundResource(R.drawable.custom_rounded_backgroud);
                    GradientDrawable dr = (GradientDrawable) gridView.getChildAt(current_Selected).getBackground();
                    dr.setColor(Color.parseColor(BackgroundColorResource.get(current_Selected)));

                    // changing the current selection to what user has clicked on
                    current_Selected = position;


                    // Selects the particular item and changes its background
                    view.setBackgroundResource(R.drawable.round_white);
                    LayerDrawable drawable = (LayerDrawable) view.getBackground();
                    drawable.setColorFilter(Color.parseColor(BackgroundColorResource.get(current_Selected)), PorterDuff.Mode.DST_OVER);
                }

                // sends the changes to the activity
                // Reference https://www.youtube.com/watch?v=i22INe14JUc&list=PLrnPJCHvNZuBkhcesO6DfdCghl6ZejVPc&index=2
                String cl = BackgroundColorResource.get(current_Selected);
                listener.onInputASend(cl);
            }
        });

        // show keyboard button at the top left corner of the bottom sheet
        ImageButton showKeyboard = v.findViewById(R.id.show_keyboard_1);
        showKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCallKeyboardA(1);
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof BackgroundChangeListener){
            listener = (BackgroundChangeListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement Background listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}