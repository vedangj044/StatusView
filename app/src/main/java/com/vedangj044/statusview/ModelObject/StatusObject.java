package com.vedangj044.statusview.ModelObject;

import java.util.ArrayList;
import java.util.List;

public class StatusObject {

    private int NumberOfStatus;
    private String SharedByUid, SharedByName, SharedTime, SharedByProfileImage;
    private List<StatusObject> StatusObjectOfDifferentTypes = new ArrayList<>();

    /*
    * NumberOfStatus -> The number of status images. This variable decides number of progress bar
    *                   and the length of array containing the image url.
    * Shared by uid -> unique id assigned to the user at the time of the registration can be
    *                   used to dereference the user name and profile picture
    * Shared by name -> name of the user
    * Shared by Profile Image -> profile picture of the image
    *
    * StatusObjectOfDifferentTypes -> stores the image, text and video object in the same list
    *
    * The length of both array should be equal to the NumberOfStatus
    *
    * */

    public StatusObject() {
    }

    public StatusObject(int numberOfStatus, String sharedByUid, String sharedByName, String sharedTime, String sharedByProfileImage, List<StatusObject> statusObjectOfDifferentTypes) {
        NumberOfStatus = numberOfStatus;
        SharedByUid = sharedByUid;
        SharedByName = sharedByName;
        SharedTime = sharedTime;
        SharedByProfileImage = sharedByProfileImage;
        StatusObjectOfDifferentTypes = statusObjectOfDifferentTypes;
    }

    public int getNumberOfStatus() {
        return NumberOfStatus;
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

    public List<StatusObject> getStatusObjectOfDifferentTypes() {
        return StatusObjectOfDifferentTypes;
    }

    public void setStatusObjectOfDifferentTypes(List<StatusObject> statusObjectOfDifferentTypes) {
        StatusObjectOfDifferentTypes = statusObjectOfDifferentTypes;
    }
}
