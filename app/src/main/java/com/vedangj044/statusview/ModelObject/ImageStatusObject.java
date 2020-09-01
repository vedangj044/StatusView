package com.vedangj044.statusview.ModelObject;

public class ImageStatusObject extends StatusObject {

    private String thumbnailURL, ImageURL;

    /*
    * ThumnailImageURL = base64 string of the original image
    * ImageUrl = url of the original image
    *
    * */

    public ImageStatusObject(String thumbnailURL, String imageURL) {
        this.thumbnailURL = thumbnailURL;
        ImageURL = imageURL;
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
}
