package com.vedangj044.statusview.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import org.mp4parser.Container;
//import org.mp4parser.muxer.Track;
//import org.mp4parser.muxer.Movie;
//import org.mp4parser.muxer.builder.DefaultMp4Builder;
//import org.mp4parser.muxer.container.mp4.MovieCreator;
//import org.mp4parser.muxer.tracks.AppendTrack;
//import org.mp4parser.muxer.tracks.ClippedTrack;

import com.theartofdev.edmodo.cropper.CropImage;
import com.vedangj044.statusview.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.vedangj044.statusview.RangeSeekBar;
import com.vedangj044.statusview.TrimVideoUtils;
import com.vedangj044.statusview.commonvideolibrary.MediaHelper;
import com.vedangj044.statusview.commonvideolibrary.Resolution;
import com.vedangj044.statusview.commonvideolibrary.SamplerClip;
import com.vedangj044.statusview.commonvideolibrary.VideoResampler;
import com.vedangj044.statusview.videocompressor.VideoCompress;

import org.xmlpull.v1.XmlPullParser;

import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import life.knowledge4.videotrimmer.utils.BackgroundExecutor;
import life.knowledge4.videotrimmer.view.TimeLineView;

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

    private RangeSeekBar rangeSeekBar;
    private int endTime;
    private int startTime;

    private RelativeLayout timeLineLayout;
    private TextView durationText;

    private OnTrimVideoListener onTrimVideoListener;


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

        rangeSeekBar = findViewById(R.id.range_seek_bar);
        timeLineLayout = findViewById(R.id.containerTimeLine);

        durationText = findViewById(R.id.duration_text);

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

                        //
                    }
                    else{
                        trimVideo(startTime/1000,endTime/1000,path);
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

        rangeSeekBar.setOnRangeBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangerListener() {
            @Override
            public void onIndexChange(RangeSeekBar rangeSeekBar, int i, int i1) {
                int duration = (i1 - i);

                String text = String.format(Locale.getDefault(), "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

                durationText.setText(text);
                duration = duration/1000;

                if(duration > 31){
                    rangeSeekBar.setRightIndex(i+30000);
                }
                if(i != startTime){
                    VideoStatus.seekTo(i);
                }
//                Log.v("asss", ""+i+" "+i1);
                startTime = i;
                endTime = i1;
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
        timeLineLayout.setVisibility(View.GONE);
        VideoStatus.setVisibility(View.GONE);
        ImageStatus.setVisibility(View.VISIBLE);
    }

    // Set the video view according to the video URI
    public void addVideo(String path){

        // adding media controls
        MediaController mediaController = new MediaController(this);
        VideoStatus.setVideoPath(path);
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
        int count = attr.getAttributeCount();
        Log.v("As", String.valueOf(count));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        params.setMargins(10, 10, 10, 10);

        timeLineLayout.addView(timeLineView, params);

        timeLineView.setVideo(Uri.fromFile(new File(path)));


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
               rangeSeekBar.setTickCount(duration);
               rangeSeekBar.setLeftIndex(duration);
               rangeSeekBar.setRightIndex(0);
           }
       });
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

    public void trimVideo(int startMs, int endMs, String path){

        String fileName = "newVideo.mp4";
        File saveTrimmedVideo = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String time = new SimpleDateFormat("yyyyMMddss").format(new Date())+".mp4";
        int duration = (endMs - startMs)/1000;

        //  ffmpeg -ss 823.2 -t 44.1 -i input.mp4 -ss 1074.1 -t 27.3 -i input.mp4 -filter_complex "[0:v][0:a][1:v][1:a]concat=n=2:v=1:a=1[v][a]" -map "[v]" -map "[a]" output.mp4
        //  ffmpeg -ss 823.2 -t 44.1 -i input.mp4 -ss 1074.1 -t 27.3 -i input.mp4 -filter_complex "[0:v][0:a][1:v][1:a]concat=n=2:v=1:a=1[v][a]" -map "[v]" -map "[a]" -c:v libx265 -crf 35 -preset slow output.mp4

        File fff = new File(saveTrimmedVideo, time);
        String[] comm = {"-i", path, "-ss", "0", "-t", "5", "-async", "1", "-c", "copy", "output.mp4"};

        String command  = "-ss 1 -i "+path+" -to 5 -c copy "+fff.getAbsolutePath();
        String command1  = "-ss 1 -i "+path+" -to 5 -c copy  -vcodec libx265 -crf 28 "+fff.getAbsolutePath();

//        TrimVideoUtils.startTrim(path, fff.getAbsolutePath(), startTime, endTime);

//        new TrimTask().execute(Uri.parse(path), Uri.parse(fff.getAbsolutePath()));
        String ui = saveTrimmedVideo.getAbsolutePath() + "/2020091549.mp4";
        Uri.parse(ui);
//        mux(Uri.parse(path).toString(), Uri.parse(fff.getAbsolutePath()).toString());

//        int rc = FFmpeg.execute(command1);
//
//
//        if (rc == RETURN_CODE_SUCCESS) {
//            Toast.makeText(this, "Done !", Toast.LENGTH_SHORT).show();
//            Log.i(Config.TAG, "Command execution completed successfully.");
//        } else if (rc == RETURN_CODE_CANCEL) {
//            Log.i(Config.TAG, "Command execution cancelled by user.");
//        } else {
//            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
//            Config.printLastCommandOutput(Log.INFO);
//        }

        Uri mSrc = Uri.parse(path);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, mSrc);
        long METADATA_KEY_DURATION = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        final File file = new File(mSrc.getPath());

        int mTimeVideo = (endTime - startTime)/1000;
        int MIN_TIME_FRAME = 1000;
        int mEndPosition = endTime;
        int mStartPosition = startTime;

        if (mTimeVideo < MIN_TIME_FRAME) {

            if ((METADATA_KEY_DURATION - mEndPosition) > (MIN_TIME_FRAME - mTimeVideo)) {
                mEndPosition += (MIN_TIME_FRAME - mTimeVideo);
            } else if (mStartPosition > (MIN_TIME_FRAME - mTimeVideo)) {
                mStartPosition -= (MIN_TIME_FRAME - mTimeVideo);
            }
        }

        onTrimVideoListener = new OnTrimVideoListener() {
            @Override
            public void onTrimStarted() {

            }

            @Override
            public void getResult(Uri uri) {
                String out = uri.toString().replace(".mp4", "_compress.mp4");
                VideoCompress.VideoCompressTask task = VideoCompress.compressVideoLow(uri.toString(), out, new VideoCompress.CompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onProgress(float percent) {

                    }
                });
            }

            @Override
            public void cancelAction() {

            }

            @Override
            public void onError(String message) {

            }
        };

        int finalMStartPosition = mStartPosition;
        int finalMEndPosition = mEndPosition;
        BackgroundExecutor.execute(
                new BackgroundExecutor.Task("", 0L, "") {
                    @Override
                    public void execute() {
                        try {
                            String out = "/storage/emulated/0/Android/data/com.vedangj044.statusview/files/Download/";
                            TrimVideoUtils.startTrim(file, out, finalMStartPosition, finalMEndPosition, onTrimVideoListener);
                        } catch (final Throwable e) {
                            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                        }
                    }
                }
        );

    }

    class TrimTask extends AsyncTask<Uri, Void, Uri> {

        @Override
        protected Uri doInBackground( Uri... uris ) {

            if ( uris.length < 2 ) {
                return null;
            }

            Uri inputUri = uris[0];
            Uri outputUri = uris[1];

            VideoResampler resampler = new VideoResampler();
            SamplerClip clip = new SamplerClip( inputUri );
            clip.setStartTime( startTime );
            clip.setEndTime( endTime );
            resampler.addSamplerClip( clip );

            // resampler.setInput( inputUri );
            resampler.setOutput( outputUri );

            Resolution mOutputResolution = Resolution.RESOLUTION_480P;

            int mInputHeight = MediaHelper.GetHeight(inputUri);
            int mInputWWidth = MediaHelper.GetWidth(inputUri);

            Log.v("IMpir", mInputHeight+""+mInputWWidth);
            if(mInputHeight > mInputWWidth){
                mOutputResolution = mOutputResolution.rotate();
            }

            int mOutputBitRate = 512000;
            int mOutputFrameRate = 30;
            int mOutputIFrameInterval = 5;

            resampler.setOutputResolution( mOutputResolution.getWidth(), mOutputResolution.getHeight() );
            resampler.setOutputBitRate( mOutputBitRate );
            resampler.setOutputFrameRate( mOutputFrameRate );
            resampler.setOutputIFrameInterval( mOutputIFrameInterval );


            // resampler.setStartTime( mTrimStart );
            // resampler.setEndTime( mTrimEnd );

            try {
                resampler.start();
//                mux(inputUri.toString(), outputUri.toString());
            } catch ( Throwable e ) {
                e.printStackTrace();
            }

            return outputUri;
        }

        @Override
        protected void onPostExecute( Uri outputUri ) {
            Toast.makeText(UploadActivity.this, "Done 1", Toast.LENGTH_SHORT);
        }
    }

//    public void trimFun(String url){
//
//        Movie movie = null;
//        try {
//            movie = new MovieCreator().build(url);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        double startTime1 = 10;
//        double endTime1 = 20;
//        double startTime2 = 30;
//        double endTime2 = 40;
//
//        boolean timeCorrected = false;
//
//        List<Track> tracks = movie.getTracks();
//        movie.setTracks(new LinkedList<>());
//
//        // Here we try to find a track that has sync samples. Since we can only start decoding
//        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
//        // such a frame
//        for (Track track : tracks) {
//            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
//                if (timeCorrected) {
//                    // This exception here could be a false positive in case we have multiple tracks
//                    // with sync samples at exactly the same positions. E.g. a single movie containing
//                    // multiple qualities of the same video (Microsoft Smooth Streaming file)
//
//                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
//                }
//                startTime1 = correctTimeToSyncSample(track, startTime1, false);
//                endTime1 = correctTimeToSyncSample(track, endTime1, true);
//                startTime2 = correctTimeToSyncSample(track, startTime2, false);
//                endTime2 = correctTimeToSyncSample(track, endTime2, true);
//                timeCorrected = true;
//            }
//        }
//
//        for (Track track : tracks) {
//            long currentSample = 0;
//            double currentTime = 0;
//            double lastTime = -1;
//            long startSample1 = -1;
//            long endSample1 = -1;
//            long startSample2 = -1;
//            long endSample2 = -1;
//
//            for (int i = 0; i < track.getSampleDurations().length; i++) {
//                long delta = track.getSampleDurations()[i];
//
//
//                if (currentTime > lastTime && currentTime <= startTime1) {
//                    // current sample is still before the new starttime
//                    startSample1 = currentSample;
//                }
//                if (currentTime > lastTime && currentTime <= endTime1) {
//                    // current sample is after the new start time and still before the new endtime
//                    endSample1 = currentSample;
//                }
//                if (currentTime > lastTime && currentTime <= startTime2) {
//                    // current sample is still before the new starttime
//                    startSample2 = currentSample;
//                }
//                if (currentTime > lastTime && currentTime <= endTime2) {
//                    // current sample is after the new start time and still before the new endtime
//                    endSample2 = currentSample;
//                }
//                lastTime = currentTime;
//                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
//                currentSample++;
//            }
//            try {
//                movie.addTrack(new AppendTrack(new ClippedTrack(track, startSample1, endSample1), new ClippedTrack(track, startSample2, endSample2)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        long start1 = System.currentTimeMillis();
//        Container out = new DefaultMp4Builder().build(movie);
//        long start2 = System.currentTimeMillis();
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(String.format("output-%f-%f--%f-%f.mp4", startTime1, endTime1, startTime2, endTime2));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        FileChannel fc = fos.getChannel();
//        try {
//            out.writeContainer(fc);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            fc.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        long start3 = System.currentTimeMillis();
//    }
//
//    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
//        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
//        long currentSample = 0;
//        double currentTime = 0;
//        for (int i = 0; i < track.getSampleDurations().length; i++) {
//            long delta = track.getSampleDurations()[i];
//
//            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
//                // samples always start with 1 but we start with zero therefore +1
//                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
//            }
//            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
//            currentSample++;
//
//        }
//        double previous = 0;
//        for (double timeOfSyncSample : timeOfSyncSamples) {
//            if (timeOfSyncSample > cutHere) {
//                if (next) {
//                    return timeOfSyncSample;
//                } else {
//                    return previous;
//                }
//            }
//            previous = timeOfSyncSample;
//        }
//        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
//    }
//
//    public Uri mux(String audioFile, String videoFile) {
//        String outputFilePath = videoFile.replace(".mp4", "_with_audio.mp4");
//        Uri outputFile = Uri.parse(outputFilePath);
//        Movie video;
//
////        try {
////            video = new MovieCreator().build(videoFile);
////        } catch (RuntimeException e) {
////            e.printStackTrace();
////            return null;
////        } catch (IOException e) {
////            e.printStackTrace();
////            return null;
////        }
//
//
//        Movie audio;
//        try {
//            audio = new MovieCreator().build(audioFile);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        Movie movie = null;
//        try {
//            movie = new MovieCreator().build(audioFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            // audioFile is my original big videoFile. Index 0 is video, index 1 is audio  in Tracks.
//            // if you have a media file with only audio index can be 0. Be carefull
//
//
////            movie =  new MovieCreator().build(audioFile);
//            movie.setTracks(new LinkedList<>());
//
////            long currentSample = 0;
////            double currentTime = 0;
////            double lastTIme = -1;
////            long startSample = -1;
////            long endSample = -1;
////            for (Track track: audio.getTracks()){
////                for(int i = 0; i < track.getSampleDurations().length; i++){
////                    if(currentTime > lastTIme && currentTime <= startTime) {
////                        startSample = currentSample;
////                    }
////                    if(currentTime > lastTIme && currentTime <= endTime){
////                        endSample = currentSample;
////                    }
////                    else{
////                        break;
////                    }
////                    lastTIme = currentTime;
////                    currentTime += (double) track.getSampleDurations()[i]/(double)track.getTrackMetaData().getTimescale();
////                    currentSample++;
//////                    Log.v( "startTrim: ", startSample + " " + endSample);
////
////                }
////                Log.v( "startTrim: " , startTime + " " + endTime);
////                movie.addTrack(new CroppedTrack(track, startSample, endSample));
////
////            }
////            video.addTrack(movie.getTracks().get(1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        org.mp4parser.Container out = new DefaultMp4Builder().build(movie);
//
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(outputFile.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//        BufferedWritableFileByteChannel byteBufferByteChannel = new BufferedWritableFileByteChannel(fos);
//        try {
////            Toast.makeText(UploadActivity.this, "Done 2", Toast.LENGTH_SHORT);
//
//            out.writeContainer(byteBufferByteChannel);
//            byteBufferByteChannel.close();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return outputFile;
//    }
//
//    private static class BufferedWritableFileByteChannel implements WritableByteChannel {
//        private static final int BUFFER_CAPACITY = 1000000;
//
//        private boolean isOpen = true;
//        private final OutputStream outputStream;
//        private final ByteBuffer byteBuffer;
//        private final byte[] rawBuffer = new byte[BUFFER_CAPACITY];
//
//        private BufferedWritableFileByteChannel(OutputStream outputStream) {
//            this.outputStream = outputStream;
//            this.byteBuffer = ByteBuffer.wrap(rawBuffer);
//        }
//
//        @Override
//        public int write(ByteBuffer inputBuffer) throws IOException {
//            int inputBytes = inputBuffer.remaining();
//
//            if (inputBytes > byteBuffer.remaining()) {
//                dumpToFile();
//                byteBuffer.clear();
//
//                if (inputBytes > byteBuffer.remaining()) {
//                    throw new BufferOverflowException();
//                }
//            }
//
//            byteBuffer.put(inputBuffer);
//
//            return inputBytes;
//        }
//
//        @Override
//        public boolean isOpen() {
//            return isOpen;
//        }
//
//        @Override
//        public void close() throws IOException {
//            dumpToFile();
//            isOpen = false;
//        }
//
//        private void dumpToFile() {
//            try {
//                outputStream.write(rawBuffer, 0, byteBuffer.position());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}