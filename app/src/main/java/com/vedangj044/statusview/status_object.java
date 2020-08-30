package com.vedangj044.statusview;

import java.util.ArrayList;
import java.util.List;

public class status_object {

    private int NumberOfStatus;
    private String SharedByUid, SharedByName, SharedTime, SharedByProfileImage;
    private List<String> StatusImageURL = new ArrayList<>();
    private List<String> ThumbnailURL = new ArrayList<>();

    /*
    * NumberOfStatus -> The number of status images. This variable decides number of progress bar
    *                   and the length of array containing the image url.
    * Shared by uid -> unique id assigned to the user at the time of the registration can be
    *                   used to dereference the user name and profile picture
    * Shared by name -> name of the user
    * Shared by Profile Image -> profile picture of the image
    *
    * Status Image URL -> Array containing the Url of the images of the status
    *
    * Thumbnail URL -> Array containing the URL of the thumbnail images
    *
    * The length of both array should be equal to the NumberOfStatus
    *
    * */

    public status_object(int numberOfStatus, String sharedByUid, String sharedByName, String sharedTime, String sharedByProfileImage, List<String> statusImageURL, List<String> thumbnailURL) {
        NumberOfStatus = numberOfStatus;
        SharedByUid = sharedByUid;
        SharedByName = sharedByName;
        SharedTime = sharedTime;
        SharedByProfileImage = sharedByProfileImage;
        StatusImageURL = statusImageURL;
        ThumbnailURL = thumbnailURL;
    }

    public int getNumberOfStatus() {
        return NumberOfStatus;
    }

    public List<String> getThumbnailURL() {
        return ThumbnailURL;
    }

    public void setThumbnailURL(List<String> thumbnailURL) {
        ThumbnailURL = thumbnailURL;
    }

    public void setNumberOfStatus(int numberOfStatus) {
        NumberOfStatus = numberOfStatus;
    }

    public String getSharedByUid() {
        return SharedByUid;
    }

    public void setSharedByUid(String sharedByUid) {
        SharedByUid = sharedByUid;
    }

    public String getSharedByName() {
        return SharedByName;
    }

    public void setSharedByName(String sharedByName) {
        SharedByName = sharedByName;
    }

    public String getSharedTime() {
        return SharedTime;
    }

    public void setSharedTime(String sharedTime) {
        SharedTime = sharedTime;
    }

    public String getSharedByProfileImage() {
        return SharedByProfileImage;
    }

    public void setSharedByProfileImage(String sharedByProfileImage) {
        SharedByProfileImage = sharedByProfileImage;
    }

    public List<String> getStatusImageURL() {
        return StatusImageURL;
    }

    public void setStatusImageURL(List<String> statusImageURL) {
        StatusImageURL = statusImageURL;
    }
}
