package com.vedangj044.statusview.Animation;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.vedangj044.statusview.Activity.MainActivity;
import com.vedangj044.statusview.Activity.StatusDisplay;

import java.util.List;

public class ProgressBarAnimationView extends LinearLayout{

    private List<CustomProgressBar> listOfStatus;
    private int currentIndex;

    public interface OnChange{
        void sendSignal(Integer currentIndex);
        void fin();
    }

    private OnChange listener;

    public void setListener(OnChange listener){
        this.listener = listener;
    }

    public ProgressBarAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentDuration(int duration){
        Log.v("sda", ""+duration);
        listOfStatus.get(currentIndex).setDuration(duration);
    }

    public void setInfo( List<CustomProgressBar> listOfStatus, int currentIndex){
        this.listOfStatus = listOfStatus;
        this.currentIndex = currentIndex;
        populate();
    }

    private void populate(){
        for(CustomProgressBar cb: listOfStatus){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);

            layoutParams.weight = listOfStatus.size();
            cb.setLayoutParams(layoutParams);

            this.addView(cb);
        }
    }

    public void start(Boolean flag){
        if(flag){
            listener.sendSignal(currentIndex);
        }

        listOfStatus.get(currentIndex).start();
        listOfStatus.get(currentIndex).setOnEventProgressBar(new CustomProgressBar.OnEventProgressBar() {
            @Override
            public void onComplete() {
                if(currentIndex != listOfStatus.size() - 1){
                    next(false);
                }
                else{
                    listener.fin();
                }
            }
        });
    }

    public int getCurrentOn(){
        return listOfStatus.get(currentIndex).currentOn;
    }

    public void next(Boolean outside){
        if(outside){
            listOfStatus.get(currentIndex).end();
        }
        else{
            currentIndex += 1;
            start(true);
        }
    }

    public void previous(){
        if(currentIndex != 0){
            listOfStatus.get(currentIndex).reset();
            currentIndex -= 1;
            start(true);
        }
    }

    public void pause(){
        listOfStatus.get(currentIndex).pause();
    }

    public void play(){
        listOfStatus.get(currentIndex).play();
    }

}
