package com.vedangj044.statusview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class create_text_status extends AppCompatActivity {

    private RelativeLayout backgroundOfText;
    private EditText StatusContent;
    private FloatingActionButton sendStatus;

    private String currentBackground = "#778899";
    private int currentFont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_status);

        backgroundOfText = findViewById(R.id.text_relative_layout_backgroud);
        StatusContent = findViewById(R.id.edit_text_content);
        sendStatus = findViewById(R.id.send_status);


        TextView fontChangeButton = findViewById(R.id.change_font);
        ImageButton changeBackgroundColor = findViewById(R.id.change_background_color);

        final List<String> BackgroundColorResource = new ArrayList<>();
        BackgroundColorResource.add("#778899");
        BackgroundColorResource.add("#290001");
        BackgroundColorResource.add("#f8bd7f");
        BackgroundColorResource.add("#4e89ae");
        BackgroundColorResource.add("#206a5d");
        BackgroundColorResource.add("#ffc93c");
        BackgroundColorResource.add("#1a1a2e");
        BackgroundColorResource.add("#2d4059");
        BackgroundColorResource.add("#382933");

        final List<Integer> textFont = new ArrayList<>();
        textFont.add(R.font.cantata_one);
        textFont.add(R.font.carter_one);
        textFont.add(R.font.homemade_apple);
        textFont.add(R.font.allerta_stencil);
        textFont.add(R.font.architects_daughter);
        textFont.add(R.font.delius_unicase_bold);
        textFont.add(R.font.varela_round);

        changeBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current =  BackgroundColorResource.indexOf(currentBackground);
                if(current == BackgroundColorResource.size() - 1){
                    current = -1;
                }
                currentBackground = BackgroundColorResource.get(current + 1);
                backgroundOfText.setBackgroundColor(Color.parseColor(currentBackground));
            }
        });

        fontChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFont == textFont.size() - 1){
                    currentFont = 0;
                }
                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), textFont.get(currentFont++));
                StatusContent.setTypeface(typeface);
            }
        });

        StatusContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(StatusContent.getText().toString().equals("")){
                    sendStatus.setVisibility(View.GONE);
                }
                else{
                    sendStatus.setVisibility(View.VISIBLE);
                }
            }
        });

        sendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_status_object t1 = new text_status_object(StatusContent.getText().toString(),
                        currentBackground, textFont.get(currentFont));

                Toast.makeText(getApplicationContext(), t1.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}