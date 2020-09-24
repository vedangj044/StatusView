package com.vedangj044.statusview.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedangj044.statusview.Activity.UploadActivity;
import com.vedangj044.statusview.Adapters.GalleryImageAdapter;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView TimePeriod, SelectedItem;
    private List<String> selectedItemList;
    private ImageView SelectionDone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);

        // RecyclerView
        recyclerView = view.findViewById(R.id.gallery_recycle);

        // Grid spanCount is set to 4
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 4));

        // TextView denotes the Month of Media Capture
        TimePeriod = view.findViewById(R.id.text_to_sort);

        // TextView denotes the count of selected item
        SelectedItem = view.findViewById(R.id.selected_item_count);

        // Button to confirm the selection
        SelectionDone = view.findViewById(R.id.selection_done);

        // Adapted object is created
        // Count is set to 0
        // Go through comments of GalleryImageAdapter to know about importance of count variable
        GalleryImageAdapter grp = new GalleryImageAdapter(view.getContext(), 0);

        // Listener
        grp.setOnNotifyImageData(new GalleryImageAdapter.notifyImageDate() {
            @Override
            public void sendSignal(String text) {

                // Month is displayed
                TimePeriod.setText(text);
            }

            @Override
            public void sendSelectedItem(List<String> s) {

                // SelectedItemList array is updated
                selectedItemList = s;

                // Count of the selected item is changed
                SelectedItem.setText(String.valueOf(s.size()));
                if(s.size() > 0){
                    // Confirm checkMark is displayed only when more than 1 item selected
                    SelectionDone.setVisibility(View.VISIBLE);
                }
                else {
                    SelectedItem.setText("");
                    SelectionDone.setVisibility(View.GONE);
                }
            }
        });


        SelectionDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent Containing the list of path of all images is send to the UploadActivity
                Intent intent = new Intent(view.getContext(), UploadActivity.class);
                intent.putStringArrayListExtra("imageList", (ArrayList<String>) selectedItemList);
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(grp);

        return view;
    }
}
