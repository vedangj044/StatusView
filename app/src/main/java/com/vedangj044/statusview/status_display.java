package com.vedangj044.statusview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
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

    private TextView textStatus;
    private RelativeLayout backgroundLayout;

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

        //base64 image url
        String imgBase64 = "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";

        // List of status object can store both image and text types of status
        List<status_object> StatusList = new ArrayList<>();
        StatusList.add(new text_status_object("Hello, Vedang Joshi", "#206a5d", 2131296259));
        StatusList.add(new image_status_object(imgBase64, "https://media.wired.com/photos/5a593a7ff11e325008172bc2/125:94/w_2393,h_1800,c_limit/pulsar-831502910.jpg"));
        StatusList.add(new image_status_object(imgBase64, "https://f4.bcbits.com/img/a2322320532_10.jpg"));
        StatusList.add(new image_status_object(imgBase64, "https://media.wired.com/photos/5a593a7ff11e325008172bc2/125:94/w_2393,h_1800,c_limit/pulsar-831502910.jpg"));
        StatusList.add(new image_status_object(imgBase64, "https://f4.bcbits.com/img/a2322320532_10.jpg"));

        // creating status object
        st1 = new status_object(5, "1000", "Vedang Joshi",
                "12:57PM", "https://www.nicepng.com/png/detail/340-3400381_smiling-man-smiling-man-face-png.png", StatusList);


        // Information about the person who posted the status is displayed
        sharedByName = findViewById(R.id.shared_by);
        sharedByTime = findViewById(R.id.shared_time);
        sharedByProfilePic = findViewById(R.id.shared_by_profile_pic);
        backgroundLayout = findViewById(R.id.background_layout);
        textStatus = findViewById(R.id.status_text);
        statusImageUrl = findViewById(R.id.status_image);

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

        // empty the textView and imageView and set background to null
        clearScreen();

        // Get the progress bar object of the current status
        final ProgressBar pg = (ProgressBar) progressBarHolder.getChildAt(index);

        // animation
        anim =  new ProgressBarAnimation(pg, 0, 100);

        // Loader is visible
        final ProgressBar loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        // Duration to fill the progress bar
        // THIS SHOULD BE EQUAL TO THE COUNT DOWN TIMER
        anim.setDuration(DEFAULT_TIME_STATUS_VIEW);


        // When the object is an instance of the image_Status_object meaning its an image
        if(st1.getStatusObjectOfDifferentTypes().get(index) instanceof image_status_object){

            String URL =  ((image_status_object) st1.getStatusObjectOfDifferentTypes().get(index)).getImageURL();
            String ThumbnailURL = ((image_status_object) st1.getStatusObjectOfDifferentTypes().get(index)).getThumbnailURL();

            // Using Glide to load and cache the image
            Glide.with(getApplicationContext()).load(URL)
                    // used override because image was resizing automatically
                    .override(Target.SIZE_ORIGINAL)
                    // While the original image loads a blurred copy is fetched and displayed
                    .thumbnail(Glide.with(getApplicationContext()).load(Base64.decode(ThumbnailURL, Base64.DEFAULT)).override(1000))
                    // Added listener
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
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

        // When the object is a text status
        else if(st1.getStatusObjectOfDifferentTypes().get(index) instanceof text_status_object){

            text_status_object t1 = (text_status_object) st1.getStatusObjectOfDifferentTypes().get(index);

            // Loader in invisible
            loading.setVisibility(View.GONE);
            // time is started
            pg.startAnimation(anim);

            // background color is set to what user has defined in the text status object
            backgroundLayout.setBackgroundColor(Color.parseColor(t1.getColorString()));
            // content is set
            textStatus.setText(t1.getContent());


            // font is changed
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), t1.getFontNumber());
            textStatus.setTypeface(typeface);

            try {
                timer.start();
            }
            catch (Exception e){
                DefaultTimerForStatusChange();
                timer.start();
            }

            // incrementing the index
            current++;

        }


    }

    // Restores default before change in status
    private void clearScreen() {

        backgroundLayout.setBackgroundResource(0);
        textStatus.setText("");
        statusImageUrl.setImageDrawable(null);

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
