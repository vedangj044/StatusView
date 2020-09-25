package com.vedangj044.statusview.Stickers;

import java.util.ArrayList;
import java.util.List;

public class AllStickerModel {

    private String title;
    private String logo;
    private List<String> imageURL = new ArrayList<>();

    public AllStickerModel(String title, String logo, List<String> imageURL) {
        this.title = title;
        this.logo = logo;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getImageURL() {
        if(imageURL  == null){
            return new ArrayList<String>();
        }
        return imageURL;
    }

    public void setImageURL(List<String> imageURL) {
        this.imageURL = imageURL;
    }
}
