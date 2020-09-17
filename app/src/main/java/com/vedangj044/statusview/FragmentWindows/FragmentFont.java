package com.vedangj044.statusview.FragmentWindows;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import com.vedangj044.statusview.ListOfResource;
import com.vedangj044.statusview.R;

import java.util.List;

public class FragmentFont extends Fragment {

    // Interface to send messages back to activity
    FontStyleChangeListener li;

    // array to hold all font integer value
    final List<Integer> textFont = ListOfResource.textFont;
    // array to hold the font color value
    final List<Integer> fontColor = ListOfResource.fontColor;

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
        void onInputCSend(int color);

        // shows the keyboard in CreateTextStatus activity
        void onCallKeyboardB(int i);
    }

    // set the listener to trigger if user interacts with the popUpWindow
    public void setFontStyleChangeListener (FontStyleChangeListener listener){
        this.li = listener;
    }

    // When the popUpWindow is create setState function is called, it takes in the
    // already selected font and fontColor and style them accordingly.
    // creating a persistent behavior
    // it is similar to onSavedInstanceBundle
    public void setState(int currentFontSelectedId, int currentFontColorSelectedId){
        this.currentFontColorSelectedId = currentFontColorSelectedId;
        this.currentFontSelectedId = currentFontSelectedId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_text_select, container, false);

        LinearLayout layout = view.findViewById(R.id.font_style_selection_list);

        // click listener to change font style
        View.OnClickListener fontSelect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                // triggers goes back to activity
                li.onInputBSend(id);

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

        // styling and dynamically adding the text font based on the textFont array
        for(int i = 0; i < textFont.size(); i++){
            TextView textView = new TextView(view.getContext());

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
            textView.setTextColor(view.getContext().getResources().getColor(R.color.iconBackgrouf));
            textView.setTextSize(20);

            // font style
            Typeface typeface = ResourcesCompat.getFont(view.getContext(), textFont.get(i));
            textView.setTypeface(typeface);

            // id, click listener
            textView.setId(i);
            textView.setOnClickListener(fontSelect);
            layout.addView(textView);
        }

        // Similar to above approach we populate the font color change layout



        LinearLayout fontColorLayout = view.findViewById(R.id.font_color_selection_list);

        View.OnClickListener fontColorChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                // 100 offset is set so that id doesn't clash with text view ids
                id = id - 100;

                // send message back to activity
                li.onInputCSend(id);

                // if a color is already selected then restore its styling to default
                if(currentFontColorSelectedId != -1){

                    CircularImageView ig = view.findViewById(currentFontColorSelectedId+100);
                    ig.setBackgroundResource(R.drawable.round_profile_pic);

                    setColor(ig, view.getContext().getResources().getString(fontColor.get(currentFontColorSelectedId)));
                }

                // change the current selected font color
                currentFontColorSelectedId = id;

                // replaces current border with a thick one
                v.setBackgroundResource(R.drawable.round_profile_pic_thick);
                setColor(v, view.getContext().getResources().getString(fontColor.get(currentFontColorSelectedId)));
            }
        };

        // Already selected font color is send to the fragment by the activity on creation
//        currentFontColorSelectedId = fontColor.indexOf(getArguments().getString("currentFontColor"));

        for(int i = 0; i < fontColor.size(); i++){
            // circular image view is an open source library for circular image views
            CircularImageView imageItem = new CircularImageView(view.getContext());

            if(currentFontColorSelectedId == i){
                imageItem.setBackgroundResource(R.drawable.round_profile_pic_thick);
            }
            else{
                imageItem.setBackgroundResource(R.drawable.round_profile_pic);
            }
            // background with white border


            // background color
            GradientDrawable drawable = (GradientDrawable) imageItem.getBackground();
            drawable.setColorFilter(Color.parseColor(view.getContext().getResources().getString(fontColor.get(i))), PorterDuff.Mode.MULTIPLY);

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
                li.onCallKeyboardB(1);
            }
        });


        return view;
    }

    private void setColor(View v, String s) {
        GradientDrawable drawable = (GradientDrawable) v.getBackground();
        drawable.setColorFilter(Color.parseColor(s), PorterDuff.Mode.MULTIPLY);
    }
}
