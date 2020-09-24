package com.vedangj044.statusview.Animation;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProgressBarAnimationView extends LinearLayout {


    // List of progress bar instance
    List<ProgressBar> listOfStatus;

    // default interval value for countdown timer
    private static int currentCountDownTimerValue = 100;

    // current index of status
    private int currentIndex;

    // current elapsed time of status view
    private int currentOn;

    // total number of status
    private int numberOfStatus;

    // context
    private Context context;

    // count down timer instance = This instance should be made null before starting a
    // new one
    private CountDownTimer currentTimerInstance;

    // duration of the current status
    private int duration;

    public ProgressBarAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setInfo( int numberOfStatus ){
        this.currentIndex = 0;
        this.numberOfStatus = numberOfStatus;
        this.listOfStatus = new ArrayList<>();
        populate();
    }

    // populates the linear layout
    private void populate(){

        for(int i = 0; i < numberOfStatus; i++){

            ProgressBar pg = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);

            // weight divided equally
            layoutParams.weight = numberOfStatus;
            pg.setLayoutParams(layoutParams);

            this.addView(pg);
            this.listOfStatus.add(pg);
        }

    }

    private OnStatusChangeListener listener;

    public interface OnStatusChangeListener{
        // request start of the next method
        void requestStart(int currentIndex);

        // call to finish activity
        void fin();
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener){
        this.listener = listener;
    }

    // sets the current duration of the current progress bar
    public void setCurrentDuration(int duration){

        this.duration = duration;
        listOfStatus.get(currentIndex).setMax(duration);

        // Timer instance is create in this function and started later
        currentTimerInstance = new CountDownTimer(duration, currentCountDownTimerValue) {
            @Override
            public void onTick(long millisUntilFinished) {
                listOfStatus.get(currentIndex).setProgress(currentOn);
                currentOn += currentCountDownTimerValue;
            }

            @Override
            public void onFinish() {
                next(false);
            }
        };

    }

    public void start(){
        currentTimerInstance.start();
    }

    // next function takes boolean argument
    // force = true -> there is exist a count down timer that needs to cancelled first
    // force = false -> there is no live count down timer instance
    public void next(Boolean force){

        currentOn = 0;


        if(force){
            currentTimerInstance.cancel();
        }

        listOfStatus.get(currentIndex).setProgress(Math.max(duration, 100));
        duration = 0;

        // check if last status is viewing
        currentIndex += 1;
        if(currentIndex < numberOfStatus){
            listener.requestStart(currentIndex);
        }
        else{
            listener.fin();
        }

    }

    // previous method
    public void previous(){

        currentOn = 0;
        duration = 0;

        currentTimerInstance.cancel();
        listOfStatus.get(currentIndex).setProgress(0);

        currentIndex -= 1;

        if(currentIndex < 0){
            currentIndex = 0;
        }

        listener.requestStart(currentIndex);
    }

    // there is pause functionality is count down timer thus
    // the current instance is cancelled and a new is created later
    public void pause(){

        currentTimerInstance.cancel();

    }

    // creates a new instance with duration
    // equal to duration - currentOn
    // i.e remaining time of status
    public void play(){

        currentTimerInstance = new CountDownTimer(duration - currentOn, currentCountDownTimerValue) {
            @Override
            public void onTick(long millisUntilFinished) {
                listOfStatus.get(currentIndex).setProgress(currentOn);
                currentOn += currentCountDownTimerValue;
            }

            @Override
            public void onFinish() {
                listener.requestStart(currentIndex);
            }
        }.start();

    }
}
