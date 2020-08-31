package com.vedangj044.statusview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView viewedRecyclerView;
    private RecyclerView.LayoutManager viewedlayoutManager;
    private status_adapter viewedStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewedRecyclerView = findViewById(R.id.viewedRecyclerView);
        // recycler view

        viewedlayoutManager = new GridLayoutManager(this, 2);
        viewedRecyclerView.setLayoutManager(viewedlayoutManager);
        // used grid layout manager to inflate a grid and not a list.
        // span count refers to the number of entries in a single row of the grid


        // create list to hold hardcoded values.
        List<thumbnail_status> tempData = new ArrayList<>();

        thumbnail_status th1 = new thumbnail_status("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png",
                "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", "1000", "Vedang Joshi", 2);
        thumbnail_status th2 = new thumbnail_status("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png",
                "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", "1000", "Vedang Joshi", 2);
        thumbnail_status th3 = new thumbnail_status("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png",
                "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", "1000", "Vedang Joshi", 2);

        tempData.add(th1);
        tempData.add(th2);
        tempData.add(th3);

        // attached adapter
        viewedStatusAdapter = new status_adapter(tempData);
        viewedRecyclerView.setAdapter(viewedStatusAdapter);

        FloatingActionButton createText = findViewById(R.id.create_text_status);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, create_text_status.class);
                startActivity(intent);
            }
        });

    }

}