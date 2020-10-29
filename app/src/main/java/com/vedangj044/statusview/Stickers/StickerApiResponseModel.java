package com.vedangj044.statusview.Stickers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StickerApiResponseModel {

    private String message;
    private String status;

    @SerializedName("data")
    private List<StickerApiResponsePrimaryHelper> data;

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

    public List<StickerApiResponsePrimaryHelper> getData() {
        return data;
    }

    public void setData(List<StickerApiResponsePrimaryHelper> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StickerApiResponseModel{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
