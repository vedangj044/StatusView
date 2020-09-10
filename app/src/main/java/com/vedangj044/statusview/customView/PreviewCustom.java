package com.vedangj044.statusview.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;

public class PreviewCustom extends PreviewView {

    // This overrides the performClick function
    // to support accessibility options
    // https://stackoverflow.com/questions/47107105/android-button-has-setontouchlistener-called-on-it-but-does-not-override-perform

    Context mContext;

    // Interface to send signals to main activity
    public touchListener mTouchListener;

    public PreviewCustom(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public PreviewCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PreviewCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                performClick();
                mTouchListener.receiveSignal(true);
                return true;
        }
        return false;
    }

    @Override
    public boolean performClick(){
        super.performClick();
        return true;
    }

    public void setmTouchListener(touchListener listener){
        this.mTouchListener = listener;
    }

    // Send signal to activity
    public interface touchListener{
        void receiveSignal(boolean event);
    }
}
