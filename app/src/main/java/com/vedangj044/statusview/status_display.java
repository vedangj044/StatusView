package com.vedangj044.statusview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class status_display extends AppCompatActivity {

    // Object for the top liner layout where we dynamically add progress bars
    private LinearLayout progressBarHolder;

    // Status Object is the object conatining entire data receivec from server
    // These are objects to display the data
    private TextView sharedByName, sharedByTime;
    private ImageView sharedByProfilePic;
    private ImageView statusImageUrl;

    // Timer to trigger the status image change
    private CountDownTimer timer;

    // IMPORTANT This variables stores the index of the status currently being viewed
    private int current = 0;

    // Status object
    private status_object st1;

    // Animation object to start and stop along with status image change
    private ProgressBarAnimation anim;

    // Time one of the status is displayed
    private int DEFAULT_TIME_STATUS_VIEW = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_display);

        // List of hardcoded urls for the status
        List<String> tempStatusURLs = new ArrayList<>();
        tempStatusURLs.add("https://media.wired.com/photos/5a593a7ff11e325008172bc2/125:94/w_2393,h_1800,c_limit/pulsar-831502910.jpg");
        tempStatusURLs.add("https://f4.bcbits.com/img/a2322320532_10.jpg");
        tempStatusURLs.add("https://media.wired.com/photos/5a593a7ff11e325008172bc2/125:94/w_2393,h_1800,c_limit/pulsar-831502910.jpg");
        tempStatusURLs.add("https://f4.bcbits.com/img/a2322320532_10.jpg");
        tempStatusURLs.add("https://media.wired.com/photos/5a593a7ff11e325008172bc2/125:94/w_2393,h_1800,c_limit/pulsar-831502910.jpg");

        // List of thumbnail url for the status
        // this has to be the low quality version of the above urls
        List<String> thumbnailsURLS = new ArrayList<>();
        thumbnailsURLS.add("https://iili.io/dPOFEP.md.jpg");
        thumbnailsURLS.add("https://iili.io/dPOFEP.md.jpg");
        thumbnailsURLS.add("https://iili.io/dPOFEP.md.jpg");
        thumbnailsURLS.add("https://iili.io/dPOFEP.md.jpg");
        thumbnailsURLS.add("https://iili.io/dPOFEP.md.jpg");

        // creating status object
        st1 = new status_object(5, "1000", "Vedang Joshi",
                "12:57PM", "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", tempStatusURLs, thumbnailsURLS);


        // Information about the person who posted the status is displayed
        sharedByName = findViewById(R.id.shared_by);
        sharedByTime = findViewById(R.id.shared_time);
        sharedByProfilePic = findViewById(R.id.shared_by_profile_pic);

        sharedByName.setText(st1.getSharedByName());
        sharedByTime.setText(st1.getSharedTime());
        Glide.with(this).load(st1.getSharedByProfileImage()).into(sharedByProfilePic);

        // Linear Layout at the top
        progressBarHolder = findViewById(R.id.progress_bar_holder);

        // Loop runs numberOfStatus times
        // A new progress bar is added this many times
        for(int i = 0; i < st1.getNumberOfStatus(); i++){

            // Progress bar object
            ProgressBar statusViewingTimer = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            statusViewingTimer.setTag(i);
            statusViewingTimer.setMax(100);

            // Layout parameter - margins
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);

            // Weight of layout params equally divides the screen width to each progress bar
            layoutParams.weight = st1.getNumberOfStatus();
            statusViewingTimer.setLayoutParams(layoutParams);

            // Add view
            progressBarHolder.addView(statusViewingTimer);
        }

        // Invisible Button covering 80% of the screen
        Button nextButton = findViewById(R.id.next_status);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextProgressBar();
            }
        });

        // Invisible Button covering 20% of the screen
        Button previousButton = findViewById(R.id.previous_status);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousProgressBar();
            }
        });

        // Start progress bar is the MAIN function responsible to
        // Start progress bar Animation
        // Loading Thumbnail image
        // Loader
        // Loading Status Image
        // Starting 5 second timer
        startProgressBar(current);

        DefaultTimerForStatusChange();

    }

    private void DefaultTimerForStatusChange(){

        // Timer object
        timer = new CountDownTimer(DEFAULT_TIME_STATUS_VIEW, 100) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                // if the index is on the last status then timer is cancelled
                if(current == st1.getNumberOfStatus()){
                    timer.cancel();
                }
                else{
                    startProgressBar(current);
                }
            }
        };

    }

    private void startProgressBar(int index){

        // Get the progress bar object of the current status
        final ProgressBar pg = (ProgressBar) progressBarHolder.getChildAt(index);

        // animation
        anim =  new ProgressBarAnimation(pg, 0, 100);

        statusImageUrl = findViewById(R.id.status_image);

        // Loader is visible
        final ProgressBar loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        List<String> url = st1.getStatusImageURL();

        // Duration to fill the progress bar
        // THIS SHOULD BE EQUAL TO THE COUNT DOWN TIMER
        anim.setDuration(DEFAULT_TIME_STATUS_VIEW);

        // Using Glide to load and cache the image
        Glide.with(getApplicationContext()).load(url.get(index))
                // used override because image was resizing automatically
                .override(Target.SIZE_ORIGINAL)
                // While the original image loads a blurred copy is fetched and displayed
                .thumbnail(Glide.with(getApplicationContext()).load(st1.getThumbnailURL().get(index)).override(Target.SIZE_ORIGINAL))
                // Added listener
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.v("failed", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Loader is gone
                        loading.setVisibility(View.GONE);
                        // The progress bar starts to fill ONLY when the status image is completely loaded
                        pg.startAnimation(anim);

                        // Added try/catch as it was causing a null pointer exception
                        try {
                            timer.start();
                        }
                        catch (Exception e){
                            DefaultTimerForStatusChange();
                            timer.start();
                        }

                        // Increment the index of status
                        current++;

                        return false;
                    }
                }).into(statusImageUrl);


    }

    // When next status is clicked
    private void nextProgressBar(){
        // current animation is cancelled
        anim.cancel();

        // If the index is on the last status then do nothing
        if(current < st1.getNumberOfStatus()){
            startProgressBar(current);
        }

        // All the progress bar before this status are filled
        fillAllProgressBarUpto(current);
    }

    private void fillAllProgressBarUpto(int current) {

        // loop to fill all the progess bar
        for(int i = 0; i < current; i++){
            final ProgressBar previous = (ProgressBar) progressBarHolder.getChildAt(i);

            // using Runnable as setProgress directly was causing unexpected behaviour
            previous.post(new Runnable() {
                @Override
                public void run() {
                    previous.setProgress(100);
                }
            });
        }
    }

    // When previous button is clicked
    private void previousProgressBar(){
        // current animation is cancelled
        anim.cancel();

        // if the index is on the first status then do nothing
        current = Math.max(current - 2, 0);
        startProgressBar(current);

        // To empty all the progress bar next to this
        emptyAllProgressBarFrom(current);
    }

    private void emptyAllProgressBarFrom(int current) {

        // Loop to setProgess to zero
        for(int i = current; i < st1.getNumberOfStatus(); i++){
            ProgressBar next = (ProgressBar) progressBarHolder.getChildAt(i);
            next.setProgress(0);
        }
    }
}
