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

    // Background for text view
    private RelativeLayout backgroundOfText;
    // Text View for status
    private EditText StatusContent;
    // Button to upload the status
    private FloatingActionButton sendStatus;

    // current background string value
    private String currentBackground = "#778899";
    // current font index
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


        // List of all color resources
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

        // List of all font resources
        final List<Integer> textFont = new ArrayList<>();
        textFont.add(R.font.cantata_one);
        textFont.add(R.font.carter_one);
        textFont.add(R.font.homemade_apple);
        textFont.add(R.font.allerta_stencil);
        textFont.add(R.font.architects_daughter);
        textFont.add(R.font.delius_unicase_bold);
        textFont.add(R.font.varela_round);


        // When the color plate icon is clicked it
        // changes the background by moving to the next index in the list
        // if it encounters the last index then set the index to 0
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


        // When the T button at the button is clicked
        // it changes the font of the Edit text and if the encounters the last index in the font
        // list then sets it to 0
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


        // An change listener for the edit text.
        // The upload button is only visible if the edit text is not null
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


        // For now, When the upload button is clicked it calls the on string method
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