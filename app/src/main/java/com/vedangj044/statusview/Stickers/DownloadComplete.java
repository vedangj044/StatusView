package com.vedangj044.statusview.Stickers;

import androidx.lifecycle.MutableLiveData;

import com.badoo.mobile.util.WeakHandler;

import java.io.File;
import java.util.HashMap;

public class DownloadComplete {

    public static MutableLiveData<HashMap<Integer, Boolean>> downloadMap = new MutableLiveData<>();

    public DownloadComplete(int packCode, String lastFile){

        if(!downloadMap.getValue().containsKey(packCode)){
            checkDownload(packCode, lastFile);
        }
    }

    public static void checkDownload(final int packCode, final String imageUrl) {

        final WeakHandler mHandler = new WeakHandler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkDownload(packCode, imageUrl);
            }
        };

        File file = new File(imageUrl);
        if(!file.exists()){
            mHandler.postDelayed(runnable, 1);
        }
        else{
            downloadMap.getValue().put(packCode, true);
            downloadMap.setValue(downloadMap.getValue());
        }
    }


}
