package com.vedangj044.statusview.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Filter;
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
import com.vedangj044.statusview.ModelObject.ImageStatusObject;
import com.vedangj044.statusview.ModelObject.StatusObject;
import com.vedangj044.statusview.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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


        sendButton = findViewById(R.id.upload_status);

        // Send button click event
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path != null){
                    if(!isVideo(path)){
                        // Bitmap is saved to the app file directory
                        Bitmap compressedBitmap = getCompressedBitmap(BitmapFactory.decodeFile(path));

                        File file = new File(getApplicationContext().getFilesDir(), "1.png");
                        try(FileOutputStream out = new FileOutputStream(file)){
                            compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        }
                        catch (IOException e){}
                        Bitmap thumbnail = getThumbnailBitmap(compressedBitmap);
                    }
                }
                else{
                    List<String> path;
                    for(int  i = 0; i < lp.getChildCount(); i++){
                        // Bitmap is saved to the app file directory

                        File file = new File(getApplicationContext().getFilesDir(), String.valueOf(i)+".png");
                        try(FileOutputStream out = new FileOutputStream(file)){
                            String p = arg.get(lp.getChildAt(i).getId());
                            if(!isVideo(p)){
                                Bitmap compressedBitmap = getCompressedBitmap(BitmapFactory.decodeFile(p));
                                compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                Toast.makeText(UploadActivity.this, "saved!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (IOException e){}
                    }
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

        ImageStatus.setImageBitmap(bmp);
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

    public Bitmap getCompressedBitmap(Bitmap bmp){
        // compression of image happens here
        int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);

        return scaled;
    }

    // Generates the thumbnail for the bitmap
    public Bitmap getThumbnailBitmap(Bitmap bm) {

        // Scale by which image should be reduced
        int reduction = 100;

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

        return resizedBitmap;
    }
}