package com.vedangj044.statusview;

public class SettingStatusModel {

    String thumbnail;
    int viewCount;
    String updateTime;

    public SettingStatusModel(String thumbnail, int viewCount, String updateTime) {
        this.thumbnail = thumbnail;
        this.viewCount = viewCount;
        this.updateTime = updateTime;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
