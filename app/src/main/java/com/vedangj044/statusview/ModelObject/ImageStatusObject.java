package com.vedangj044.statusview.ModelObject;

public class ImageStatusObject extends StatusObject {

    private String thumbnailURL, ImageURL;
    private Boolean isVideo;

    /*
    * ThumnailImageURL = base64 string of the original image
    * ImageUrl = url of the original image
    *
    * */

    public ImageStatusObject(String thumbnailURL, String imageURL) {
        this.thumbnailURL = thumbnailURL;
        ImageURL = imageURL;
    }

    public ImageStatusObject(String thumbnailURL, String imageURL, Boolean isVideo) {
        this.thumbnailURL = thumbnailURL;
        ImageURL = imageURL;
        this.isVideo = isVideo;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    @Override
    public String toString() {
        return "ImageStatusObject{" +
                "thumbnailURL='" + thumbnailURL + '\'' +
                ", ImageURL='" + ImageURL + '\'' +
                '}';
    }
}
