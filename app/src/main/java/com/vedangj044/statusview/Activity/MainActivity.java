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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Installing the Custom Emoji Provider class at the start of the app
        EmojiManager.install(new CEmojiProvider());

        // MainActivityFragment Instance
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mainActivityFragment).commit();

    }

}