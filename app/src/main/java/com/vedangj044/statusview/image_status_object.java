package com.vedangj044.statusview;

import java.util.List;

public class image_status_object extends status_object {

    private String thumbnailURL, ImageURL;

    /*
    * ThumnailImageURL = base64 string of the original image
    * ImageUrl = url of the original image
    *
    * */

    public image_status_object(String thumbnailURL, String imageURL) {
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
