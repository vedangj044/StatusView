package com.vedangj044.statusview.FragmentWindows;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vedangj044.statusview.Adapters.BackgroudColorAdapter;
import com.vedangj044.statusview.ListOfResource;
import com.vedangj044.statusview.R;

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
    private List<Integer> BackgroundColorResource = ListOfResource.BackgroundColorResource;

    // Interface to send message back to the activity
    // Change commit messages
    public interface BackgroundChangeListener{
        // send the color string value
        void onInputASend(int backgroundResource);

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
        BackgroudColorAdapter bgAdapter = new BackgroudColorAdapter(v.getContext(), BackgroundColorResource,  current_Selected);
        gridView.setAdapter(bgAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("aaa", String.valueOf(position));
                Log.v("aaa", String.valueOf(current_Selected));

                if(current_Selected  != -1){
                    RelativeLayout rp = (RelativeLayout) gridView.findViewById(current_Selected);
                    try{
                        rp.findViewById(current_Selected+100).setBackgroundResource(0);
                    }
                    catch (NullPointerException e){
                        e.getMessage();
                    }

                }


                ImageView item = (ImageView) view.findViewById(position+100);
                item.setBackgroundResource(R.drawable.ic_tick_foreground);
                current_Selected = position;

                listener.onInputASend(position);
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
