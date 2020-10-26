package com.vedangj044.statusview.Stickers;

import java.util.List;

public class StickerApiResponseHelper {

    private StickerCategoryModel category;

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
}
