package com.vedangj044.statusview.Stickers;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "StickerModel", foreignKeys = {@ForeignKey(entity = StickerCategoryModel.class,
parentColumns = "categoryId",
childColumns = "stickerCategoryId",
onDelete = CASCADE)})
public class StickerModel {

    @Ignore
    private int id;

    @ColumnInfo(name = "stickerCategoryId")
    private int stickerCategoryId;

    @PrimaryKey
    @ColumnInfo(name = "code")
    @NonNull
    private String code;

    @ColumnInfo(name = "image")
    private String image;

    public StickerModel(String code, int stickerCategoryId, String image) {
        this.stickerCategoryId = stickerCategoryId;
        this.image = image;
        this.code = code;
    }

    @Ignore
    public StickerModel(String image) {
        this.image = image;
    }

    public int getStickerCategoryId() {
        return stickerCategoryId;
    }

    public void setStickerCategoryId(int stickerCategoryId) {
        stickerCategoryId = stickerCategoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
