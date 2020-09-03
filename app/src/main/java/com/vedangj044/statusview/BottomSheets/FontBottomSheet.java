package com.vedangj044.statusview.BottomSheets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class FontBottomSheet extends Fragment {

    // Interface to send messages back to activity
    private FontStyleChangeListener listener;

    // array to hold all font integer value
    final List<Integer> textFont = new ArrayList<>();
    // array to hold the font color value
    final List<String> fontColor = new ArrayList<>();

    // default padding
    private static final int padding = 10;
    // default width for font color circular image view
    private static final int width = 50;

    // Padding for text view 10dp
    private int paddingTextView;

    // current fontStyleSelectedState
    private int currentFontSelectedId = -1;

    // current font color selected id
    private int currentFontColorSelectedId = -1;

    // interface to send message back to activity
    public interface FontStyleChangeListener{
        // send font style change event
        void onInputBSend(int a);

        // send font color change event
        void onInputCSend(String color);

        // shows the keyboard in CreateTextStatus activity
        void onCallKeyboardB(int i);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_text_select, container, false);

        textFont.add(R.font.cantata_one);
        textFont.add(R.font.carter_one);
        textFont.add(R.font.homemade_apple);
        textFont.add(R.font.allerta_stencil);
        textFont.add(R.font.architects_daughter);
        textFont.add(R.font.delius_unicase_bold);
        textFont.add(R.font.varela_round);

        LinearLayout layout = view.findViewById(R.id.font_style_selection_list);

        // click listener to change font style
        View.OnClickListener fontSelect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                // triggers goes back to activity
                listener.onInputBSend(textFont.get(id));

                // if a font style is already selected then restore it to default style
                if(currentFontSelectedId != -1){
                    TextView tv = view.findViewById(currentFontSelectedId);
                    tv.setBackgroundResource(0);
                    tv.setPadding(paddingTextView, paddingTextView, paddingTextView, paddingTextView);
                }

                // change the selected style
                currentFontSelectedId = id;

                // background to have round corners
                v.setBackgroundResource(R.drawable.round_text_font_background);

                // Padding
                v.setPadding(paddingTextView, paddingTextView, paddingTextView, paddingTextView);

            }
        };

        // Already selected font is send to the fragment by the activity on creation
        currentFontSelectedId = textFont.indexOf(getArguments().getInt("currentFont", -1));

        // styling and dynamically adding the text font based on the textFont array
        for(int i = 0; i < textFont.size(); i++){
            TextView textView = new TextView(getActivity());

            // background to have round corners
            if(currentFontSelectedId == i){
                textView.setBackgroundResource(R.drawable.round_text_font_background);
            }

            // set text
            textView.setText("Text");

            // padding
            paddingTextView = Math.round(padding* Resources.getSystem().getDisplayMetrics().density);;
            textView.setPadding(paddingTextView, paddingTextView, paddingTextView, paddingTextView);

            // margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(20,10,20,10);
            textView.setLayoutParams(params);

            // color and size
            textView.setTextColor(getResources().getColor(R.color.iconBackgrouf));
            textView.setTextSize(20);

            // font style
            Typeface typeface = ResourcesCompat.getFont(getActivity(), textFont.get(i));
            textView.setTypeface(typeface);

            // id, click listener
            textView.setId(i);
            textView.setOnClickListener(fontSelect);
            layout.addView(textView);
        }

        // Similar to above approach we populate the font color change layout

        fontColor.add("#778899");
        fontColor.add("#290001");
        fontColor.add("#f8bd7f");
        fontColor.add("#4e89ae");
        fontColor.add("#206a5d");
        fontColor.add("#ffc93c");
        fontColor.add("#1a1a2e");
        fontColor.add("#2d4059");
        fontColor.add("#382933");

        LinearLayout fontColorLayout = view.findViewById(R.id.font_color_selection_list);

        View.OnClickListener fontColorChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                // 100 offset is set so that id doesn't clash with text view ids
                id = id - 100;

                // send message back to activity
                listener.onInputCSend(fontColor.get(id));

                // if a color is already selected then restore its styling to default
                if(currentFontColorSelectedId != -1){

                    CircularImageView ig = view.findViewById(currentFontColorSelectedId+100);
                    ig.setBackgroundResource(R.drawable.round_profile_pic);

                    setColor(ig, fontColor.get(currentFontColorSelectedId));
                }

                // change the current selected font color
                currentFontColorSelectedId = id;

                // replaces current border with a thick one
                v.setBackgroundResource(R.drawable.round_profile_pic_thick);
                setColor(v, fontColor.get(currentFontColorSelectedId));
            }
        };

        // Already selected font color is send to the fragment by the activity on creation
        currentFontColorSelectedId = fontColor.indexOf(getArguments().getString("currentFontColor"));

        for(int i = 0; i < fontColor.size(); i++){
            // circular image view is an open source library for circular image views
            CircularImageView imageItem = new CircularImageView(getActivity());

            if(currentFontColorSelectedId == i){
                imageItem.setBackgroundResource(R.drawable.round_profile_pic_thick);
            }
            else{
                imageItem.setBackgroundResource(R.drawable.round_profile_pic);
            }
            // background with white border


            // background color
            GradientDrawable drawable = (GradientDrawable) imageItem.getBackground();
            drawable.setColorFilter(Color.parseColor(fontColor.get(i)), PorterDuff.Mode.DST_OVER);

            // Size and width
            int p = Math.round(width* Resources.getSystem().getDisplayMetrics().density);;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(p, ViewGroup.LayoutParams.MATCH_PARENT);

            // margins
            params.setMargins(10, 10, 10, 10);
            imageItem.setLayoutParams(params);

            // id, click Listener
            // 100 is added so that id doesn't clash with text view id
            imageItem.setId(i+100);
            imageItem.setOnClickListener(fontColorChange);
            fontColorLayout.addView(imageItem);
        }


        // Triggers when top left corner keyboard button is clicked
        ImageButton showKeyboard = view.findViewById(R.id.show_keyboard_2);
        showKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCallKeyboardB(1);
            }
        });

        return view;
    }

    // set color method to set color when a background resource is already set
    private void setColor(View v, String s) {
        GradientDrawable drawable = (GradientDrawable) v.getBackground();
        drawable.setColorFilter(Color.parseColor(s), PorterDuff.Mode.DST_OVER);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof BackgroundBottomSheet.BackgroundChangeListener){
            listener = (FontStyleChangeListener) context;
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
