package com.vedangj044.statusview;

public class thumbnail_status {

    // POJO

    private String ImageUrl, StatusUrl, SharedByUID, SharedByName;
    private int numberOfStatus;

    // ImageUrl - url of the status
    // StatusUrl - url to the content that has to loaded
    // SharedByUid - can be used to deference the user name using unique id assigned to it LATER
    // SharedByName - name of the person who shared this status
    // numberOfStatus - number of status that can be view by clicking on this.

    public thumbnail_status(String imageUrl, String statusUrl, String sharedByUID, String sharedByName, int numberOfStatus) {
        ImageUrl = imageUrl;
        StatusUrl = statusUrl;
        SharedByUID = sharedByUID;
        SharedByName = sharedByName;
        this.numberOfStatus = numberOfStatus;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getStatusUrl() {
        return StatusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        StatusUrl = statusUrl;
    }

    public String getSharedByUID() {
        return SharedByUID;
    }

    public void setSharedByUID(String sharedByUID) {
        SharedByUID = sharedByUID;
    }

    public String getSharedByName() {
        return SharedByName;
    }

    public void setSharedByName(String sharedByName) {
        SharedByName = sharedByName;
    }

    public int getNumberOfStatus() {
        return numberOfStatus;
    }

    public void setNumberOfStatus(int numberOfStatus) {
        this.numberOfStatus = numberOfStatus;
    }
}
