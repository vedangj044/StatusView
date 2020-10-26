package com.vedangj044.statusview.Stickers;

import java.util.List;

public class StickerApiResponseModel {

    private String message;
    private String status;

    private List<StickerApiResponseHelper> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StickerApiResponseHelper> getData() {
        return data;
    }

    public void setData(List<StickerApiResponseHelper> data) {
        this.data = data;
    }
}
