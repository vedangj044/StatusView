package com.vedangj044.statusview.ViewCount;

public class ViewCountObject {

    private String profileImageURL, name, time;

    public ViewCountObject(String profileImageURL, String name, String time) {
        this.profileImageURL = profileImageURL;
        this.name = name;
        this.time = time;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
