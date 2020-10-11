package com.vedangj044.statusview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SettingStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_status);

        setTitle("My Status");

        RecyclerView recyclerView = findViewById(R.id.setting_status_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SettingStatusModel> mDataset = new ArrayList<>();
        mDataset.add(new SettingStatusModel("iVBORw0KGgoAAAANSUhEUgAAAAUAAAADCAIAAADUVFKvAAAAAXNSR0IArs4c6QAAAANzQklUCAgI\n" +
                "2+FP4AAAADVJREFUCJkFwYENABAMBMCv2H8YWxjBGiSvqi130vrQgMWzRLxSF5UOdZzETamTSgcv\n" +
                "dsBSPi+CIKdO7fF6AAAAAElFTkSuQmCC", 20, "12:05 pm"));

        SettingStatusAdapter adapter = new SettingStatusAdapter(mDataset);
        recyclerView.setAdapter(adapter);

    }
}