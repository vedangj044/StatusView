package com.vedangj044.statusview.PopUpWindows;


import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.vedangj044.statusview.R;


/*
Reference : https://github.com/ankushsachdeva/emojicon

We display a popUpWindow over the soft keyboard for that we first measure the height of the keyboard
then place the popUpWindow over it.

This is the primary helper class, here we measure the size of the keyboard and initialize a
popUpWindow, Views to these PopUpWindow are assigned in their respective classes.

 */

public class PopUpSetup extends PopupWindow {

    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private Boolean isOpened = false;

    // interface to set measure the height of the keyboard
    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListener;

    View ScreenLayout;
    Context context;


    // constructor
    public PopUpSetup(Context context, View screenLayout) {
        super(context);
        this.ScreenLayout = screenLayout;
        this.context = context;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // default size of keyboard
        setSize((int)context.getResources().getDimension(R.dimen.keyboard_height), WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener){
        this.onSoftKeyboardOpenCloseListener = listener;
    }

    // Size of the keyboard measured
    // and assigned to keyboard height instance variable
    public void setSizeForSoftKeyboard(){
        ScreenLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                ScreenLayout.getWindowVisibleDisplayFrame(r);

                int screenHeight = getUsableScreenHeight();
                int heightDifference = screenHeight
                        - (r.bottom - r.top);

                if (heightDifference > 100) {

                    keyBoardHeight = heightDifference;
                    setSize(WindowManager.LayoutParams.MATCH_PARENT, keyBoardHeight);
                    if(isOpened == false){
                        if(onSoftKeyboardOpenCloseListener!=null)
                            onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);
                    }
                    isOpened = true;
                    if(pendingOpen){
                        showAtBottom();
                        pendingOpen = false;
                    }
                }
                else{
                    isOpened = false;
                    if(onSoftKeyboardOpenCloseListener!=null)
                        onSoftKeyboardOpenCloseListener.onKeyboardClose();
                }
            }
        });
    }

    // setSize function for the popUpWindow
    private void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    // Important function as it sets the gravity to bottom thus overlapping the keyboard completely
    private void showAtBottom() {
        showAtLocation(ScreenLayout, Gravity.BOTTOM, 0, 0);
    }

    private int getUsableScreenHeight() {
        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    // If the keyboard is not opened yet this function waits for the keyboard to open and then
    // set the height of the popUpWindow
    public void showAtBottomPending(){
        if(isKeyBoardOpen())
            showAtBottom();
        else
            pendingOpen = true;
    }

    public Boolean isKeyBoardOpen(){
        return isOpened;
    }

    public interface OnSoftKeyboardOpenCloseListener{
        void onKeyboardOpen(int keyBoardHeight);
        void onKeyboardClose();
    }
}
