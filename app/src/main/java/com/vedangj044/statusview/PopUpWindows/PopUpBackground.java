package com.vedangj044.statusview.PopUpWindows;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.vedangj044.statusview.Adapters.BackgroudColorAdapter;
import com.vedangj044.statusview.ListOfResource;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class PopUpBackground extends PopUpSetup {


    // Layout of the background passed to the parent to measure height
    View ScreenLayout;

    // Context of the activity
    Context context;

    public PopUpBackground(Context context, View screenLayout) {
        super(context, screenLayout);
        this.context = context;
        this.ScreenLayout = screenLayout;

        // View is set Here
        View customView = createCustomView();
        setContentView(customView);
    }

    // Interface to send messaged back to the activity
    private BackgroundChangeListener listener;

    // Contains the string of color code
    private List<String> BackgroundColorResource = ListOfResource.BackgroundColorResource;

    private List<Boolean> isColor = ListOfResource.isColor;

    // Interface to send message back to the activity
    // Change commit messages
    public interface BackgroundChangeListener{
        // send the color string value
        void onInputASend(String color, int resource, boolean isColor);

        // shows the keyboard in the createTextStatus activity
        void onCallKeyboardA(int i);
    }

    // variable keeps track of which color is selected and also makes sure not more than
    // one color is selected at the same time
    private int current_Selected = -1;

    // Called before the popUpWindow is displayed to style the previously selected choice
    public void setState(String color){
        this.current_Selected = BackgroundColorResource.indexOf(color);
    }

    // Notifies the activity for the changes in user selection
    public void setBackgroundChangeListener(BackgroundChangeListener listener){
        this.listener = listener;
    }

    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.bottom_background_select, null, false);


        final GridView gridView = v.findViewById(R.id.background_selector);


        // set adapter for grid view
        BackgroudColorAdapter bgAdapter = new BackgroudColorAdapter(v.getContext(), BackgroundColorResource, isColor,  current_Selected);
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
                    if(isColor.get(current_Selected)){
                        Log.v("cuure", String.valueOf(current_Selected));
                        gridView.getChildAt(current_Selected).setBackgroundResource(R.drawable.custom_rounded_backgroud);
                        GradientDrawable dr = (GradientDrawable) gridView.getChildAt(current_Selected).getBackground();
                        dr.setColor(Color.parseColor(BackgroundColorResource.get(current_Selected)));
                    }


                    // changing the current selection to what user has clicked on
                    current_Selected = position;


                    if(isColor.get(current_Selected)){
                        // Selects the particular item and changes its background
                        view.setBackgroundResource(R.drawable.round_white);
                        LayerDrawable drawable = (LayerDrawable) view.getBackground();
                        drawable.setColorFilter(Color.parseColor(BackgroundColorResource.get(current_Selected)), PorterDuff.Mode.DST_OVER);
                    }

                }

                // sends the changes to the activity
                // Reference https://www.youtube.com/watch?v=i22INe14JUc&list=PLrnPJCHvNZuBkhcesO6DfdCghl6ZejVPc&index=2
                if(isColor.get(current_Selected)){
                    String cl = BackgroundColorResource.get(current_Selected);
                    listener.onInputASend(cl, -1, true);
                }
                else{
                    int cl = Integer.parseInt(BackgroundColorResource.get(current_Selected));
                    listener.onInputASend("", cl, false);
                }
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
}
