package com.vedangj044.statusview.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import com.bumptech.glide.Glide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vedangj044.statusview.ModelObject.MediaPreview;
import com.vedangj044.statusview.ModelObject.ImageStatusObject;
import com.vedangj044.statusview.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.vedangj044.statusview.CustomView.RangeSeekBarView.RangeSeekBar;
import com.vedangj044.statusview.VideoCompressionUtils.TrimVideoUtils;
import com.vedangj044.statusview.VideoCompressionUtils.VideoConverter;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;

import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import life.knowledge4.videotrimmer.utils.BackgroundExecutor;
import life.knowledge4.videotrimmer.view.TimeLineView;

public class UploadActivity extends AppCompatActivity {

    private static int MAX_DURATION  = 30000;

    private FloatingActionButton sendButton;
    private ImageView imageView;
    private RelativeLayout parentLayout;
    private ImageView profilePicture;
    private ImageButton CropRotation;

    private ProgressBar progressBar;

    // Array maintains the list of path of the images/videos which is received from intent
    private List<String> arg;

    // horizontal layout at the bottom when multi-selected
    private LinearLayout lp;

    private ImageView ImageStatus;
    private VideoView VideoStatus;

    private ImageButton deleteIcon;

    private int currentImage = -1;
    private RangeSeekBar rangeSeekBar;


    private RelativeLayout timeLineLayout;
    private TextView durationText;

    // List contains mediaPreview objects
    private List<MediaPreview> mediaPreviewsList;


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

        // Status preview
        ImageStatus = findViewById(R.id.image_status);
        VideoStatus = findViewById(R.id.video_status);

        rangeSeekBar = findViewById(R.id.range_seek_bar);
        timeLineLayout = findViewById(R.id.containerTimeLine);

        durationText = findViewById(R.id.duration_text);

        mediaPreviewsList = new ArrayList<>();

        // On click listener to change image in view when multiple entries
        View.OnClickListener listener = v -> {
            currentImage = v.getId();
            MediaPreview m1 = getIdOfMedia(currentImage);

            if(m1.isVideo()){
                addVideo(m1);
            }
            else{
                addImage(m1);
            }

        };

        // Intent is received from GalleyViewFragment
        arg = getIntent().getStringArrayListExtra("imageList");

        // MediaPreviewList is populated and the index is assigned as ID to each
        for(String path: arg){
            mediaPreviewsList.add(new MediaPreview(path, arg.indexOf(path)));
        }

        // When there are more than one entries we need to populate the bottom horizontal scroll layout
        if(mediaPreviewsList.size() > 1) {
            for (MediaPreview s1 : mediaPreviewsList) {
                ImageView img = new ImageView(this);

                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimensionInDp, LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(10, 10, 10, 10);

                img.setLayoutParams(params);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // ID is assigned equal to ID of mediaPreview
                img.setId(s1.getId());

                img.setOnClickListener(listener);

                Glide.with(this).load(s1.getPath()).into(img);
                lp.addView(img);

                MediaPreview m1 = getIdOfMedia(0);

                moveNext();
            }
        }
        else{

            // otherwise when only one entry is present it is displayed
            currentImage = 0;
            MediaPreview m1 = mediaPreviewsList.get(0);
            if (m1.isVideo()) {
                addVideo(m1);
            } else {
                addImage(m1);
            }
        }


        // Image cropping activity
        // Reference https://github.com/ArthurHub/Android-Image-Cropper
        CropRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // View is updated
                MediaPreview m1 = getIdOfMedia(currentImage);
                if(!m1.isVideo()){
                    CropImage.activity(Uri.fromFile(new File(m1.getPath()))).start(UploadActivity.this);
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

                    // when an status is discarded it is deleted from mediaPreviewList as well
                    mediaPreviewsList.remove(getIdOfMedia(currentImage));
                }
                if(mediaPreviewsList.size() == 0){
                    finish();
                }
                else{
                    moveNext();
                }
            }
        });


        sendButton = findViewById(R.id.upload_status);
        progressBar = findViewById(R.id.progessbar);

        // Send button click event
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // FINAL LIST containing all the url required to sent to server
                List<ImageStatusObject> compressedPath = new ArrayList<>();

                // Iteration to mediaPreviewList
                for(MediaPreview m1: mediaPreviewsList){

                    if(m1.isVideo()){
                        progressBar.setVisibility(View.VISIBLE);
                        // When video trimming is completed this listener is called
                        OnTrimVideoListener onTrimVideoListener = new OnTrimVideoListener() {
                           @Override
                           public void onTrimStarted() { }

                           @Override
                           public void getResult(Uri uri) {

                               // When video is trimmed, we have to compress it
                               final String outputVideoFileDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                               final File outputVideoFileDirUri = new File(outputVideoFileDir);
                               final Uri trimmedVideoURL = uri;

                               final String thumbnail = getVideoThumbnail(uri.toString());

                               // thread handles compression of video
                               Thread thread = new Thread(){
                                   @Override
                                   public void run() {

                                       // Handles rotation of video
                                       MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                       retriever.setDataSource(UploadActivity.this, trimmedVideoURL);

                                       int width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                       int height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                       int rotation = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));

                                       if(rotation != 0){
                                           width = 0;
                                           height = 0;
                                       }

                                       boolean isConverted = VideoConverter.getInstance().convertVideo(UploadActivity.this,
                                               trimmedVideoURL,
                                               outputVideoFileDirUri, width, height, 0);

                                       if (isConverted) {

                                           // when compression is complete the compressPath list is updated
                                           ImageStatusObject img1 = new ImageStatusObject(thumbnail, VideoConverter.cachedFile.getPath(), true);
                                           compressedPath.add(img1);

                                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                                               @Override
                                               public void run() {
                                                   progressBar.setVisibility(View.GONE);
                                               }
                                           });

                                           // the trimmed video is deleted ( only compressed and trimmed video is saved )
                                           new File(uri.toString()).delete();
                                       }
                                       else{
                                           Log.v("hey", "converting");
                                       }
                                   }
                               };
                               thread.start();
                           }

                           @Override
                           public void cancelAction() {}

                           @Override
                           public void onError(String message) { }
                        };

                        // video trim function
                        videoTrim(m1, onTrimVideoListener);
                    }
                    else{

                        // Creating compressed bitmap
                        Bitmap compressedBitmap = getCompressedBitmap(BitmapFactory.decodeFile(m1.getPath()));

                        // Saving compressed bitmap
                        File file = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                        File output = new File(file, m1.getFileName());
                        try (FileOutputStream out = new FileOutputStream(output)){
                            compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }

                        // Creating Thumbnail bitmap
                        Bitmap thumbnail = getThumbnailBitmap(compressedBitmap);

                        // Saving Thumbnail bitmap
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String base64Thumbnail = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        // Creating image status object and adding to list
                        ImageStatusObject img1 = new ImageStatusObject(base64Thumbnail, output.getAbsolutePath(), false);
                        compressedPath.add(img1);

                    }

                }
            }
        });

        rangeSeekBar.setOnRangeBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangerListener() {
            @Override
            public void onIndexChange(RangeSeekBar rangeSeekBar, int i, int i1) {
                int duration = (i1 - i);

                duration = duration/1000;

                // Duration can't exceed MAX_DURATION
                if(duration > 31){
                    rangeSeekBar.setRightIndex(i + MAX_DURATION);
                }

                MediaPreview m1 = getIdOfMedia(currentImage);

                // video preview is seeked to the start of trim
                if(i != m1.getStartTime()){
                    VideoStatus.seekTo(i);
                }

                // start and end time is changed
                m1.setStartTime(i);
                m1.setEndTime(Math.min(i1, i + MAX_DURATION));

                // textView displays the duration of selected part
                durationText.setText(getDurationText(m1));
            }
        });

    }

    private void moveNext() {
        View v = lp.getChildAt(0);
        currentImage = v.getId();
        MediaPreview m1 = getIdOfMedia(currentImage);

        if(m1.isVideo()){
            addVideo(m1);
        }
        else{
            addImage(m1);
        }

    }

    // Returns thumbnail of video in Base64 string
    private String getVideoThumbnail(String uri) {

        Size mSize = new Size(96,96);
        CancellationSignal ca = new CancellationSignal();
        Bitmap bitmapThumbnail = null;

        // API 29 has content resolver method for thumbnail generation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            try {
                bitmapThumbnail = ThumbnailUtils.createVideoThumbnail(new File(uri), mSize, ca);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            bitmapThumbnail = ThumbnailUtils.createVideoThumbnail(uri, MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        
        // Thumbnail is converted to base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapThumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Thumbnail = Base64.encodeToString(byteArray, Base64.DEFAULT);


        return base64Thumbnail;
    }

    // Returns the mediaPreview object with a particular ID
    private MediaPreview getIdOfMedia(int currentImage) {

        // checks mediaPreviewList for ID
        for(MediaPreview m1: mediaPreviewsList){
            if(m1.getId() == currentImage){
                return m1;
            }
        }
        return null;
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
                if(mediaPreviewsList.size() == 1){
                    MediaPreview m1 = getIdOfMedia(currentImage);
                    m1.setPath(resultUri.getPath());
                    addImage(m1);
                }
                else{
                    ImageView img = lp.findViewById(currentImage);
                    Glide.with(UploadActivity.this).load(resultUri).into(img);
                    MediaPreview m1 = getIdOfMedia(currentImage);
                    m1.setPath(resultUri.getPath());
                    addImage(m1);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // Set the image view according to the image URI
    public void addImage(MediaPreview m1){

        CropRotation.setVisibility(View.VISIBLE);
        Bitmap bmp = BitmapFactory.decodeFile(m1.getPath());

        ImageStatus.setImageBitmap(bmp);
        timeLineLayout.setVisibility(View.GONE);
        durationText.setVisibility(View.GONE);
        VideoStatus.setVisibility(View.GONE);
        ImageStatus.setVisibility(View.VISIBLE);
    }

    // Set the video view according to the video URI
    public void addVideo(@NotNull MediaPreview m1){

        // adding media controls
        MediaController mediaController = new MediaController(this);
        VideoStatus.setVideoPath(m1.getPath());

        // TimeLine of video
        timeLineLayout.setVisibility(View.VISIBLE);

        XmlPullParser parser = getResources().getXml(R.xml.layout_timeline);

        try {
            parser.next();
            parser.nextTag();
        }catch (Exception e){
            e.printStackTrace();
        }

        AttributeSet attr  = Xml.asAttributeSet(parser);
        TimeLineView timeLineView = new TimeLineView(UploadActivity.this, attr);

        // Duration text
        durationText.setVisibility(View.VISIBLE);
        durationText.setText(getDurationText(m1));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        params.setMargins(10, 10, 10, 10);

        CropRotation.setVisibility(View.GONE);
        timeLineLayout.addView(timeLineView, params);

        timeLineView.setVideo(Uri.fromFile(new File(m1.getPath())));


        VideoStatus.requestFocus();

        ImageStatus.setVisibility(View.GONE);
        VideoStatus.setVisibility(View.VISIBLE);

        VideoStatus.setMediaController(mediaController);
        mediaController.setAnchorView(VideoStatus);
        VideoStatus.start();

        VideoStatus.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
           @Override
           public void onPrepared(MediaPlayer mp) {
               int duration = mp.getDuration();
               Log.v("max", String.valueOf(duration));

               // rangeSeekBar is set to duration
               rangeSeekBar.setTickCount(duration);

               if(m1.getEndTime() == 0){
                   m1.setStartTime(0);
                   m1.setEndTime(Math.min(duration, 30000));
               }
               rangeSeekBar.setLeftIndex(m1.getStartTime());
               rangeSeekBar.setRightIndex(m1.getEndTime());
               durationText.setText(getDurationText(m1));
           }
        });
    }

    // returns string value of minutes : seconds
    public String getDurationText(MediaPreview m1){

        int duration = m1.getEndTime() - m1.getStartTime();
        String text = String.format(Locale.getDefault(), "%d : %d ",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

        return text;
    }

    // returns compressed/scaled bitmap
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

    // Trims and saves the video
    public void videoTrim(MediaPreview m1, OnTrimVideoListener listener){

        Uri mSrc = Uri.parse(m1.getPath());
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, mSrc);
        long METADATA_KEY_DURATION = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        final File file = new File(mSrc.getPath());

        int mTimeVideo = (m1.getEndTime() - m1.getStartTime())/1000;
        int MIN_TIME_FRAME = 1000;
        int mEndPosition = m1.getEndTime();
        int mStartPosition = m1.getStartTime();

        if (mTimeVideo < MIN_TIME_FRAME) {

            if ((METADATA_KEY_DURATION - mEndPosition) > (MIN_TIME_FRAME - mTimeVideo)) {
                mEndPosition += (MIN_TIME_FRAME - mTimeVideo);
            } else if (mStartPosition > (MIN_TIME_FRAME - mTimeVideo)) {
                mStartPosition -= (MIN_TIME_FRAME - mTimeVideo);
            }
        }

        int finalMStartPosition = mStartPosition;
        int finalMEndPosition = mEndPosition;
        BackgroundExecutor.execute(
                new BackgroundExecutor.Task("", 0L, "") {
                    @Override
                    public void execute() {
                        try {
                            String out = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/";
                            Log.v("out", out);
                            TrimVideoUtils.startTrim(file, out, finalMStartPosition, finalMEndPosition, listener);
                        } catch (final Throwable e) {
                            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                        }
                    }
                }
        );
    }

}