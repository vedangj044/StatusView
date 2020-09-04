package com.vedangj044.statusview.Activity;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanniktech.emoji.EmojiPopup;
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
        ImageButton emojiPopup = findViewById(R.id.emoji_open);

        // Emoji popup builder
        final EmojiPopup emojiPopup1 = EmojiPopup.Builder.fromRootView(backgroundOfText).build(StatusContent);

        // Triggers the emoji popup
        emojiPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup1.toggle();
            }
        });


        // When the color plate icon is clicked it
        // changes the background by moving to the next index in the list
        // if it encounters the last index then set the index to 0
        changeBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // bundle is send to the fragment to keep the selection persistent
                Bundle bundle = new Bundle();
                bundle.putString("background", currentBackground);

                // Push the background selection fragment to the bottom sheet
                BackgroundBottomSheet backgroundBottomSheet = new BackgroundBottomSheet();
                backgroundBottomSheet.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.tab, backgroundBottomSheet).commit();

                // When background selection needs to displayed the keyboard is first collapses if visible
                showKeyboardShortcut(false);


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

                // bundle is sent to the fragment to keep the selection persistent
                Bundle bundle = new Bundle();
                bundle.putInt("currentFont", currentFont);
                bundle.putString("currentFontColor", currentFontColor);

                // Push the font selection fragment to the bottom sheet
                FontBottomSheet fontBottomSheet = new FontBottomSheet();
                fontBottomSheet.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.tab, fontBottomSheet).commit();

                // When font selection fragment needs to be displayed keyboard is collapsed first
                showKeyboardShortcut(false);


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
                    // Here we have hardcoded the values of maximum length of status text
                    // if it exceeds the threshold the size is reduced

                    int chara = StatusContent.getText().toString().length();
                    if(chara >= 700 || StatusContent.getLineCount() > 15){
                        // Max limit is set if any condition is met
                        StatusContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(chara)});
                        Toast.makeText(getApplicationContext(), "Content can't exceed 700 characters or 15 lines", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // Max limit is restored to 700
                        StatusContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(700)});
                    }

                    // Smallest Size
                    if(chara > 500){
                        StatusContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    }
                    // Mid size
                    else  if(chara > 300){
                        StatusContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    }
                    // Normal Size
                    else{
                        StatusContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    }
                    sendStatus.setVisibility(View.VISIBLE);
                }
            }
        });

        // preventing opening of keyboard and the bottom sheet together
        backgroundOfText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = backgroundOfText.getRootView().getHeight() - backgroundOfText.getHeight();

                DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
                float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, metrics);

                // If anything calls the keyboard it should collapse the bottom sheet
                if(heightDiff > height){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    // When show keyboard button is clicked in the font selection fragment
    @Override
    public void onCallKeyboardB(int i) {
        showKeyboardShortcut(true);
    }

    // When show keyboard button is clicked in the background selection fragment
    @Override
    public void onCallKeyboardA(int i) {
        showKeyboardShortcut(true);
    }

    private void showKeyboardShortcut(boolean t) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(t){
            // when the keyboard should be visible
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            inputManager.toggleSoftInputFromWindow(backgroundOfText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
        else{
            // when the keyboard should NOT be visible
            inputManager.hideSoftInputFromWindow(backgroundOfText.getWindowToken(), 0);
        }
    }

    // Saving state to enable screen rotation and handle configuration changes
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("currentBackground", currentBackground);
        savedInstanceState.putString("currentFontColor", currentFontColor);
        savedInstanceState.putInt("currentFont", currentFont);

        super.onSaveInstanceState(savedInstanceState);
    }

    // restoring state
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFontColor = savedInstanceState.getString("currentFontColor");
        currentBackground = savedInstanceState.getString("currentBackground");
        currentFont = savedInstanceState.getInt("currentFont");

        backgroundOfText.setBackgroundColor(Color.parseColor(currentBackground));

        if(currentFont != 0){
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), currentFont);
            StatusContent.setTypeface(typeface);
        }

        if(currentFontColor != null){
            StatusContent.setTextColor(Color.parseColor(currentFontColor));
        }

    }
}