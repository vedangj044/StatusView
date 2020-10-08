package com.vedangj044.statusview.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedangj044.statusview.Adapters.StatusAdapter;
import com.vedangj044.statusview.Maps.MapActivity;
import com.vedangj044.statusview.ModelObject.ThumbnailStatusObject;
import com.vedangj044.statusview.R;
import com.vedangj044.statusview.Stickers.StickerListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivityFragment extends Fragment {

    private RecyclerView viewedRecyclerView;
    private RecyclerView.LayoutManager viewedlayoutManager;
    private StatusAdapter viewedStatusAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        viewedRecyclerView = view.findViewById(R.id.viewedRecyclerView);
        // recycler view

        viewedlayoutManager = new GridLayoutManager(view.getContext(), 2);
        viewedRecyclerView.setLayoutManager(viewedlayoutManager);
        // used grid layout manager to inflate a grid and not a list.
        // span count refers to the number of entries in a single row of the grid


        // create list to hold hardcoded values.
        List<ThumbnailStatusObject> tempData = new ArrayList<>();

        ThumbnailStatusObject th1 = new ThumbnailStatusObject("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png",
                "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", "1000", "Vedang Joshi", 2);
        ThumbnailStatusObject th2 = new ThumbnailStatusObject("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png",
                "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", "1000", "Vedang Joshi", 2);
        ThumbnailStatusObject th3 = new ThumbnailStatusObject("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png",
                "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", "1000", "Vedang Joshi", 2);

        tempData.add(th1);
        tempData.add(th2);
        tempData.add(th3);

        // attached adapter
        viewedStatusAdapter = new StatusAdapter(tempData);
        viewedRecyclerView.setAdapter(viewedStatusAdapter);

        // Text Status Creation
        FloatingActionButton createText = view.findViewById(R.id.create_text_status);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CreateTextStatus.class);
                startActivity(intent);
            }
        });

        // Camera Image/Video Status Creation
        FloatingActionButton createCamera = view.findViewById(R.id.camera_button);
        createCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), StickerListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
