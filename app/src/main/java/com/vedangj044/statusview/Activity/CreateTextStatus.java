package com.vedangj044.statusview.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedangj044.statusview.BottomSheets.BackgroundBottomSheet;
import com.vedangj044.statusview.BottomSheets.FontBottomSheet;
import com.vedangj044.statusview.ModelObject.TextStatusObject;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class CreateTextStatus extends AppCompatActivity implements BackgroundBottomSheet.BackgroundChangeListener, FontBottomSheet.FontStyleChangeListener {

    // Background for text view
    private CoordinatorLayout backgroundOfText;
    // Text View for status
    private EditText StatusContent;
    // Button to upload the status
    private FloatingActionButton sendStatus;

    // current background string value
    private String currentBackground = "#778899";
    // current font index
    private int currentFont = 0;
    // current font color
    private String currentFontColor;

    // bottom sheer behaviour
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_status);

        backgroundOfText = findViewById(R.id.text_relative_layout_backgroud);
        StatusContent = findViewById(R.id.edit_text_content);
        sendStatus = findViewById(R.id.send_status);

        View bottomSheet = findViewById(R.id.tab_bottom);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        TextView fontChangeButton = findViewById(R.id.change_font);
        ImageButton changeBackgroundColor = findViewById(R.id.change_background_color);


        // When the color plate icon is clicked it
        // changes the background by moving to the next index in the list
        // if it encounters the last index then set the index to 0
        changeBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Push the background selection fragment to the bottom sheet
                BackgroundBottomSheet backgroundBottomSheet = new BackgroundBottomSheet();
                getSupportFragmentManager().beginTransaction().replace(R.id.tab, backgroundBottomSheet).commit();

                // bottom sheet is expanded
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // When the T button at the button is clicked
        // it changes the font of the Edit text and if the encounters the last index in the font
        // list then sets it to 0
        fontChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Push the font selection fragment to the bottom sheet
                FontBottomSheet fontBottomSheet = new FontBottomSheet();
                getSupportFragmentManager().beginTransaction().replace(R.id.tab, fontBottomSheet).commit();

                // bottom sheet is expanded
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                TextStatusObject t1 = new TextStatusObject(StatusContent.getText().toString(),
                        currentBackground, currentFont);

                Toast.makeText(getApplicationContext(), t1.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Implementing method to receive changes in the background
    @Override
    public void onInputASend(String color) {
        currentBackground = color;
        backgroundOfText.setBackgroundColor(Color.parseColor(color));
    }

    // Implementing method to receive changes in the font style
    @Override
    public void onInputBSend(int a) {
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), a);
        StatusContent.setTypeface(typeface);
        currentFont = a;
    }

    // Implementing method to receive changes in the font color
    @Override
    public void onInputCSend(String color) {
        StatusContent.setTextColor(Color.parseColor(color));
        currentFontColor = color;
    }
}