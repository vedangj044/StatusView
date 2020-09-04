package com.vedangj044.statusview.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanniktech.emoji.EmojiManager;
import com.vedangj044.statusview.Adapters.StatusAdapter;
import com.vedangj044.statusview.CustomEmoji.CEmojiProvider;
import com.vedangj044.statusview.ModelObject.ThumbnailStatusObject;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView viewedRecyclerView;
    private RecyclerView.LayoutManager viewedlayoutManager;
    private StatusAdapter viewedStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Installing the Custom Emoji Provider class at the start of the app
        EmojiManager.install(new CEmojiProvider());

        viewedRecyclerView = findViewById(R.id.viewedRecyclerView);
        // recycler view

        viewedlayoutManager = new GridLayoutManager(this, 2);
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

        FloatingActionButton createText = findViewById(R.id.create_text_status);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTextStatus.class);
                startActivity(intent);
            }
        });

    }

}