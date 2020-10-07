package com.vedangj044.statusview.Stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.vanniktech.emoji_stickers.StickerArrayAdapter;
import com.vedangj044.statusview.R;

import java.util.List;

public class FullStickerPack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_sticker_pack);

        GridView gridView = (GridView) findViewById(R.id.sticker_pack_gridview);

        List<String> urls = getIntent().getStringArrayListExtra("urllist");

        TextView textView = findViewById(R.id.title_sticker_pack);
        textView.setText(getIntent().getStringExtra("title"));

        StickerArrayAdapter st = new StickerArrayAdapter(this, urls);
        gridView.setAdapter(st);;
    }
}