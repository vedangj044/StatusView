package com.vedangj044.statusview.Stickers;

import java.util.List;

public class StickerApiModel {

    private String message;
    private String status;
    private List<StickerCategoryModel> data;



    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public List<StickerCategoryModel> getData() {
        return data;
    }
}
