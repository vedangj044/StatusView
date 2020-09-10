package com.vedangj044.statusview.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vedangj044.statusview.R;

import java.io.File;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private FloatingActionButton sendButton;
    private ImageView imageView;
    private RelativeLayout parentLayout;
    private ImageView profilePicture;
    private ImageButton CropRotation;

    // Array maintains the list of path of the images/videos
    private List<String> arg;
    private LinearLayout lp;

    private ImageView ImageStatus;
    private VideoView VideoStatus;

    private ImageButton deleteIcon;

    private int currentImage = -1;
    private int rotateAngle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Set the profile picture
        profilePicture = findViewById(R.id.profile_picture);
        Glide.with(this).load("https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png").into(profilePicture);

        // Relative Layout
        parentLayout = findViewById(R.id.parent_layout);

        // Linear layout from the bottom of the screen
        lp  = findViewById(R.id.image_selection_list);

        // ImageButton
        CropRotation = findViewById(R.id.rotation_button);
        deleteIcon = findViewById(R.id.delete_icon);

        ImageStatus = findViewById(R.id.image_status);
        VideoStatus = findViewById(R.id.video_status);

        // On click listener to change image in view when multiple entries
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImage = v.getId();
                String path = arg.get(v.getId());
                if(isVideo(path)){
                    addVideo(path);
                }
                else{
                    addImage(path);
                }
            }
        };

        // Get intent from path
        /*
        * Intent can either be a String "image"
        * OR
        * a list "imageList"
        *
        * Depending on this the layout is changed
        * */

        String path = getIntent().getStringExtra("image");
        if(path != null){
            // When only one image is selected
            if(isVideo(path)){
                addVideo(path);
            }
            else{
                addImage(path);
            }
        }
        else{
            // When multiple images are selected the bottom horizontal scroll is populated
            arg = getIntent().getStringArrayListExtra("imageList");

            for(String s1: arg){
                ImageView img = new ImageView(this);

                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimensionInDp, LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(10, 10 ,10 ,10);

                img.setLayoutParams(params);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setId(arg.indexOf(s1));

                img.setOnClickListener(listener);

                Glide.with(this).load(s1).into(img);
                lp.addView(img);
            }
        }

        // Image cropping activity
        // Reference https://github.com/ArthurHub/Android-Image-Cropper
        CropRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path != null){
                    // When only one image is selected
                    if(!isVideo(path)){
                        CropImage.activity(Uri.fromFile(new File(path))).start(UploadActivity.this);
                    }
                }
                else{
                    // When there are multiple images we have to select from arg
                    if(!isVideo(arg.get(currentImage))){
                        CropImage.activity(Uri.fromFile(new File(arg.get(currentImage)))).start(UploadActivity.this);
                    }
                }
            }
        });

        // Removes the image/video from view
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImage != -1){
                    ImageStatus.setVisibility(View.GONE);
                    VideoStatus.setVisibility(View.GONE);
                    lp.removeView(lp.findViewById(currentImage));

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Triggers when cropped image is sent back to the activity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                // change the image to the cropped image
                if(getIntent().getStringExtra("image") != null){
                    addImage(resultUri.getPath().toString());
                }
                else{
                    ImageView img = lp.findViewById(currentImage);
                    Glide.with(UploadActivity.this).load(resultUri).into(img);
                    arg.set(currentImage, resultUri.getPath().toString());
                    addImage(arg.get(currentImage));
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // Checks if the file is video or image based on extension
    public boolean isVideo(String path){
        if(path.endsWith("mp4") || path.endsWith("3gp")){
            return true;
        }
        return false;
    }

    // Set the image view according to the image URI
    public void addImage(String path){
        Bitmap bmp = BitmapFactory.decodeFile(path);

        // compression of image happens here
        int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);
        ImageStatus.setImageBitmap(scaled);

        Toast.makeText(this, "Original "+String.valueOf(bmp.getByteCount()) + " Proccessed "+ String.valueOf(scaled.getByteCount()), Toast.LENGTH_LONG).show();

        VideoStatus.setVisibility(View.GONE);
        ImageStatus.setVisibility(View.VISIBLE);
    }

    // Set the video view according to the video URI
    public void addVideo(String path){

        // adding media controls
        MediaController mediaController = new MediaController(this);
        VideoStatus.setVideoPath(path);
        VideoStatus.requestFocus();

        ImageStatus.setVisibility(View.GONE);
        VideoStatus.setVisibility(View.VISIBLE);

        VideoStatus.setMediaController(mediaController);
        mediaController.setAnchorView(VideoStatus);
        VideoStatus.start();
    }
}