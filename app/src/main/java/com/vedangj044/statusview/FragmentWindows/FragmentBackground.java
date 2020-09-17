package com.vedangj044.statusview.FragmentWindows;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vedangj044.statusview.Adapters.BackgroudColorAdapter;
import com.vedangj044.statusview.ListOfResource;
import com.vedangj044.statusview.R;

import java.util.List;

public class FragmentBackground extends Fragment {

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
    public void setState(int color){
        this.current_Selected = color;
    }

    // Notifies the activity for the changes in user selection
    public void setBackgroundChangeListener(BackgroundChangeListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.bottom_background_select, container, false);

        final GridView gridView = view.findViewById(R.id.background_selector);


        // set adapter for grid view
        BackgroudColorAdapter bgAdapter = new BackgroudColorAdapter(view.getContext(), BackgroundColorResource,  current_Selected);
        gridView.setAdapter(bgAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(current_Selected  != -1){
                    RelativeLayout rp = (RelativeLayout) gridView.findViewById(current_Selected);
                    try{
                        ImageView i = (ImageView)rp.findViewById(current_Selected+100);
                        i.setImageResource(0);
                    }
                    catch (NullPointerException e){
                        e.getMessage();
                    }

                }


                ImageView item = (ImageView) view.findViewById(position+100);
                item.setImageResource(R.drawable.ic_tick_foreground);
                current_Selected = position;

                listener.onInputASend(position);
            }
        });

        // show keyboard button at the top left corner of the bottom sheet
        ImageButton showKeyboard = view.findViewById(R.id.show_keyboard_1);
        showKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCallKeyboardA(1);
            }
        });

        return view;
    }
}
