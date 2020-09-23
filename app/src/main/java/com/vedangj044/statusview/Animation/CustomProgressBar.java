package com.vedangj044.statusview.Animation;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {

    private int duration;
    private int currentOn;
    private Context mContext;

    private CountDownTimer statusTime;

    private static int countDownInterval = 100;
    private OnEventProgressBar listener;

    public interface OnEventProgressBar{
        void onComplete();
    }

    public void setOnEventProgressBar(OnEventProgressBar listener){
        this.listener = listener;
    }

    public CustomProgressBar(Context context) {
        super(context, null, android.R.attr.progressBarStyleHorizontal);
        this.mContext = context;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        CustomProgressBar.this.setMax(duration);
    }

    public void start(){
        currentOn = 0;

        if(duration != 0){
            if(statusTime == null){
                statusTime = new CountDownTimer(duration, countDownInterval){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        CustomProgressBar.this.setProgress(currentOn);
                        currentOn += countDownInterval;
                    }

                    @Override
                    public void onFinish() {
                        CustomProgressBar.this.end();
                    }
                }.start();
            }
            statusTime.start();
        }
        else{
            Log.v("aaa"," as");
        }

    }

    public void end(){
        statusTime.cancel();
        CustomProgressBar.this.post(new Runnable() {
            @Override
            public void run() {
                CustomProgressBar.this.setProgress(duration);
            }
        });
        this.listener.onComplete();
    }

    public void reset(){
        statusTime.cancel();
        CustomProgressBar.this.setProgress(0);
    }

    public void pause(){
        statusTime.cancel();
    }

    public void play(){
        statusTime = new CountDownTimer(duration - currentOn, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                CustomProgressBar.this.setProgress(currentOn);
                currentOn += countDownInterval;
            }

            @Override
            public void onFinish() {
                CustomProgressBar.this.end();
            }
        }.start();
    }

}
