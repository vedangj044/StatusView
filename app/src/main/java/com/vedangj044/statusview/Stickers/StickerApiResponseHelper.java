package com.vedangj044.statusview.Stickers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StickerApiResponseHelper {


    private StickerCategoryModel category;

    @SerializedName("stickerImages")
    private List<StickerModel> stickerImages = null;

    public StickerCategoryModel getCategory() {
        return category;
    }

    public void setCategory(StickerCategoryModel category) {
        this.category = category;
    }

    public List<StickerModel> getStickerImages() {
        return stickerImages;
    }

    public void setStickerImages(List<StickerModel> stickerImages) {
        this.stickerImages = stickerImages;
    }

    @Override
    public String toString() {
        return "StickerApiResponseHelper{" +
                "category=" + category +
                ", stickerImages=" + stickerImages +
                '}';
    }
}
