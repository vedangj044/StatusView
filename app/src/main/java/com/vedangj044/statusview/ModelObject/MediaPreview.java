package com.vedangj044.statusview.ModelObject;

import android.webkit.MimeTypeMap;

import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MediaPreview {

    String path, compressUrl, compressedThumbnail;
    int endTime, startTime, id;
    boolean isVideo;
    String fileName;

    public MediaPreview(String url, int id) {
        this.path = url;
        this.id = id;
        this.isVideo = isVideoFinder(url);
        createFileName();
        this.startTime = this.endTime = 0;
    }

    // returns if file is video or image
    private boolean isVideoFinder(String url) {

        String mimeType = MimeTypeMap.getFileExtensionFromUrl(url);
        if(MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeType).contains("video")){
            return true;
        }
        return false;
    }

    // create file name which is IMG + timeStamp + random + extension
    private void createFileName(){
        String prefix = "";
        String extension = "";

        if(this.isVideo){
            prefix = "VID_";
            extension = ".mp4";
        }
        else{
            prefix = "IMG_";
            extension = ".png";
        }

        Random r = new Random();
        String alphabet = String.valueOf((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

        String name = prefix +
                new SimpleDateFormat("yyyyMMdd_HHmmss_", Locale.US).format(new Date()) +
                alphabet + extension;

        this.fileName = name;
    }

    // Just some getter and setter methods

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCompressedThumbnail() {
        return compressedThumbnail;
    }

    public void setCompressedThumbnail(String compressedThumbnail) {
        this.compressedThumbnail = compressedThumbnail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String url) {
        this.path = url;
    }

    public String getCompressUrl() {
        return compressUrl;
    }

    public void setCompressUrl(String compressUrl) {
        this.compressUrl = compressUrl;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
