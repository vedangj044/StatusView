package com.vedangj044.statusview.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedangj044.statusview.R;

import java.io.File;

public class UploadActivity extends AppCompatActivity {

    private FloatingActionButton sendButton;
    private ImageView imageView;
    private RelativeLayout parentLayout;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        profilePicture = findViewById(R.id.profile_picture);
        Glide.with(this).load("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png").into(profilePicture);

        parentLayout = findViewById(R.id.parent_layout);

        String path = getIntent().getStringExtra("image");
        if(path.endsWith("mp4") || path.endsWith("3gp")){
            VideoView videoView = new VideoView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            videoView.setLayoutParams(params);
            videoView.setVideoPath(path);
            videoView.requestFocus();
            videoView.start();
            parentLayout.addView(videoView);
        }
        else{
            ImageView imageView = new ImageView(this);
            Bitmap bmp = BitmapFactory.decodeFile(getIntent().getStringExtra("image"));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            imageView.setImageBitmap(bmp);
            imageView.setLayoutParams(params);
            parentLayout.addView(imageView);
        }


    }
}