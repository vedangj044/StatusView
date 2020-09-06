package com.vedangj044.statusview.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanniktech.emoji.EmojiPopup;
import com.vedangj044.statusview.ModelObject.TextStatusObject;
import com.vedangj044.statusview.PopUpWindows.PopUpBackground;
import com.vedangj044.statusview.PopUpWindows.PopUpFont;
import com.vedangj044.statusview.PopUpWindows.PopUpSetup;
import com.vedangj044.statusview.R;

public class CreateTextStatus extends AppCompatActivity{

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

    private PopUpFont popUpFont;

    private PopUpBackground popUpBackground;

    private boolean EmojiIconState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_status);

        backgroundOfText = findViewById(R.id.text_relative_layout_backgroud);
        StatusContent = findViewById(R.id.edit_text_content);
        sendStatus = findViewById(R.id.send_status);


        TextView fontChangeButton = findViewById(R.id.change_font);
        ImageButton changeBackgroundColor = findViewById(R.id.change_background_color);
        final ImageButton emojiPopup = findViewById(R.id.emoji_open);

        // Emoji popup builder
        final EmojiPopup emojiPopup1 = EmojiPopup.Builder.fromRootView(backgroundOfText).build(StatusContent);

        // Triggers the emoji popup
        emojiPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup1.toggle();
                if(EmojiIconState){
                    emojiPopup.setImageResource(R.drawable.ic_keyboard_foreground_white);
                }
                else{
                    emojiPopup.setImageResource(R.drawable.ic_emoji_foreground);
                }
                EmojiIconState = !EmojiIconState;
            }
        });

        popUpBackground = new PopUpBackground(getApplicationContext(), backgroundOfText);
        popUpBackground.setSizeForSoftKeyboard();


        popUpBackground.setOnSoftKeyboardOpenCloseListener(new PopUpSetup.OnSoftKeyboardOpenCloseListener() {
            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if(popUpBackground.isShowing()){
                    popUpBackground.dismiss();
                }
            }
        });

        popUpBackground.setBackgroundChangeListener(new PopUpBackground.BackgroundChangeListener() {
            @Override
            public void onInputASend(String color) {

                backgroundOfText.setBackgroundColor(Color.parseColor(color));
                currentBackground = color;

            }

            @Override
            public void onCallKeyboardA(int i) {
                showKeyboardShortcut(true);
            }
        });

        // When the color plate icon is clicked it
        // changes the background by moving to the next index in the list
        // if it encounters the last index then set the index to 0
        changeBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpBackground.setState(currentBackground);
                showKeyboardShortcut(true);

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(StatusContent, InputMethodManager.SHOW_IMPLICIT);

                popUpBackground.showAtBottomPending();

            }
        });

        popUpFont = new PopUpFont(getApplicationContext(), backgroundOfText);
        popUpFont.setSizeForSoftKeyboard();
        popUpFont.setOnSoftKeyboardOpenCloseListener(new PopUpSetup.OnSoftKeyboardOpenCloseListener() {
            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if(popUpFont.isShowing())
                    popUpFont.dismiss();
            }
        });

        popUpFont.setFontStyleChangeListener(new PopUpFont.FontStyleChangeListener() {
            @Override
            public void onInputBSend(int a) {

                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), a);
                StatusContent.setTypeface(typeface);
                currentFont = a;


            }

            @Override
            public void onInputCSend(String color) {

                StatusContent.setTextColor(Color.parseColor(color));
                currentFontColor = color;

            }

            @Override
            public void onCallKeyboardB(int i) {

                showKeyboardShortcut(true);

            }
        });

        // When the T button at the button is clicked
        // it changes the font of the Edit text and if the encounters the last index in the font
        // list then sets it to 0
        fontChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpFont.setState(currentFont, currentFontColor);

                showKeyboardShortcut(true);
                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(StatusContent, InputMethodManager.SHOW_IMPLICIT);
                popUpFont.showAtBottomPending();
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
                        if(StatusContent.getLineCount() > 8){
                            StatusContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        }
                        else{
                            StatusContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                        }
                    }



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

    private void showKeyboardShortcut(boolean t) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(t){
            // when the keyboard should be visible
            popUpFont.dismiss();
            popUpBackground.dismiss();
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