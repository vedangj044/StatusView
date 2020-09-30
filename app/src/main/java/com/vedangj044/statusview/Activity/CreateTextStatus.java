package com.vedangj044.statusview.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji_stickers.EmojiPopupSticker;
import com.vanniktech.emoji_stickers.stickers.Sticker;
import com.vanniktech.emoji_stickers.stickers.StickerListener;
import com.vanniktech.emoji_stickers.stickers.StickerSettings;
import com.vedangj044.statusview.FragmentWindows.FragmentBackground;
import com.vedangj044.statusview.FragmentWindows.FragmentFont;
import com.vedangj044.statusview.ListOfResource;
import com.vedangj044.statusview.ModelObject.TextStatusObject;
import com.vedangj044.statusview.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTextStatus extends AppCompatActivity{

    // Background for text view
    private RelativeLayout backgroundOfText;
    // Text View for status
    private EditText StatusContent;
    // Button to upload the status
    private FloatingActionButton sendStatus;

    // current background string value
    private int currentBackgroundResource = -1;

    // current font index
    private int currentFont = 0;
    // current font color
    private int currentFontColor;

    private FragmentFont fragmentFont;
    private FragmentBackground fragmentBackground;

    private boolean EmojiIconState = true;
    private EmojiPopupSticker emojiPopup1;

    private RelativeLayout fontFragmentContainer;
    private RelativeLayout backgroundFragmentContainer;


    private List<Integer> backgroundResourceList = ListOfResource.BackgroundColorResource;
    private List<Integer> textFont = ListOfResource.textFont;
    private List<Integer> fontColor = ListOfResource.fontColor;

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

        // relative layout container
        fontFragmentContainer = findViewById(R.id.font_fragment_container);
        backgroundFragmentContainer = findViewById(R.id.background_fragment_container);

        // fragment instances
        fragmentFont = new FragmentFont();
        fragmentBackground = new FragmentBackground();

        // adding fragment
        getSupportFragmentManager().beginTransaction().add(R.id.font_fragment_container, fragmentFont).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.background_fragment_container, fragmentBackground).commit();

        List<Sticker> stickerList = new ArrayList<>();

        stickerList.add(new Sticker("data:image/jpeg;base64,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"));
        stickerList.add(new Sticker("data:image/jpeg;base64,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"));
        stickerList.add(new Sticker("data:image/jpeg;base64,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"));
        stickerList.add(new Sticker(R.drawable.emoji_sticker_tab));

        // Emoji popup builder
        emojiPopup1 = EmojiPopupSticker.Builder.fromRootView(backgroundOfText)
                .setStickerSettings(new StickerSettings.Builder().stickers(stickerList).listener(new StickerListener() {
                    @Override
                    public void onClick(Sticker sticker) {
                        Log.v("aa", ""+sticker.toString());
                    }

                    @Override
                    public void onLongClick(Sticker sticker) {

                    }
                }).build()).build(StatusContent);

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

        fragmentBackground.setBackgroundChangeListener(new FragmentBackground.BackgroundChangeListener() {
            @Override
            public void onInputASend(int backgroundResource) {

                backgroundOfText.setBackgroundResource(backgroundResourceList.get(backgroundResource));
                currentBackgroundResource = backgroundResource;

            }

            @Override
            public void onCallKeyboardA(int i) {
                showKeyboardShortcut(true);
            }
        });

        StatusContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontFragmentContainer.setVisibility(View.GONE);
                backgroundFragmentContainer.setVisibility(View.GONE);
            }
        });

        // When the color plate icon is clicked it
        // changes the background by moving to the next index in the list
        // if it encounters the last index then set the index to 0
        changeBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // keyboard hides
                showKeyboardShortcut(false);
                if(backgroundFragmentContainer.getVisibility() == View.GONE){

                    // font container is set to GONE
                    fontFragmentContainer.setVisibility(View.GONE);
                    backgroundFragmentContainer.setVisibility(View.VISIBLE);
                }
                else{
                    backgroundFragmentContainer.setVisibility(View.GONE);
                }

            }
        });


        fragmentFont.setFontStyleChangeListener(new FragmentFont.FontStyleChangeListener() {
            @Override
            public void onInputBSend(int a) {

                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), textFont.get(a));
                StatusContent.setTypeface(typeface);
                currentFont = a;

            }

            @Override
            public void onInputCSend(int color) {

                String co = getApplicationContext().getResources().getString(fontColor.get(color));
                StatusContent.setTextColor(Color.parseColor(co));
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

                // keyboard hides
                showKeyboardShortcut(false);
                if(fontFragmentContainer.getVisibility() == View.GONE){

                    // set to GONE
                    backgroundFragmentContainer.setVisibility(View.GONE);
                    fontFragmentContainer.setVisibility(View.VISIBLE);
                }
                else{
                    fontFragmentContainer.setVisibility(View.GONE);
                }
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
                        String.valueOf(currentBackgroundResource), currentFont);

                Toast.makeText(getApplicationContext(), t1.toString(), Toast.LENGTH_SHORT).show();

                // Creating thumbnail of the text status

                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

                // base64 string of thumbnail
                String thumbnailBase64 = getThumbnailBitmap(bitmap);
                v1.setDrawingCacheEnabled(false);
            }
        });
    }

    // returns the base64 Thumb of the text status snapshot, this is
    // same as uploadActivity.getThumbnailBitmap function
    public String getThumbnailBitmap(Bitmap bm) {

        // Scale by which image should be reduced
        int reduction = 10;

        int width = bm.getWidth();
        int height = bm.getHeight();

        int newWidth = width/reduction;
        int newHeight = height/reduction;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        // Blur the scaled down image
        RenderScript rs = RenderScript.create(this);

        final Allocation input = Allocation.createFromBitmap(rs, resizedBitmap); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(resizedBitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Thumbnail = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return base64Thumbnail;
    }

    private void showKeyboardShortcut(boolean t) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(t){
            // when the keyboard should be visible
            emojiPopup1.dismiss();
            fontFragmentContainer.setVisibility(View.GONE);
            backgroundFragmentContainer.setVisibility(View.GONE);

            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            StatusContent.requestFocus();

        }
        else{
            // when the keyboard should NOT be visible
            inputManager.hideSoftInputFromWindow(backgroundOfText.getWindowToken(), 0);
        }
    }

    // Saving state to enable screen rotation and handle configuration changes
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("currentBackground", currentBackgroundResource);
        savedInstanceState.putInt("currentFontColor", currentFontColor);
        savedInstanceState.putInt("currentFont", currentFont);

        super.onSaveInstanceState(savedInstanceState);
    }

    // restoring state
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFontColor = savedInstanceState.getInt("currentFontColor");
        currentBackgroundResource = savedInstanceState.getInt("currentBackground");
        currentFont = savedInstanceState.getInt("currentFont");

        try{
            backgroundOfText.setBackgroundResource(backgroundResourceList.get(currentBackgroundResource));

            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), textFont.get(currentFont));
            StatusContent.setTypeface(typeface);

            String co = getApplicationContext().getResources().getString(fontColor.get(currentFontColor));
            StatusContent.setTextColor(Color.parseColor(co));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}