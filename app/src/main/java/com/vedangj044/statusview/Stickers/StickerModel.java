package com.vedangj044.statusview.Stickers;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "StickerModel", foreignKeys = {@ForeignKey(entity = StickerCategoryModel.class,
parentColumns = "categoryId",
childColumns = "categoryIdFk",
onDelete = CASCADE)})
public class StickerModel {

    @ColumnInfo(name = "categoryIdFk")
    private int categoryIdFk;

    @PrimaryKey
    @ColumnInfo(name = "StickerCode")
    @NonNull
    private String StickerCode;

    @ColumnInfo(name = "url")
    private String url;

    public StickerModel(String StickerCode, int categoryIdFk, String url) {
        this.categoryIdFk = categoryIdFk;
        this.url = url;
        this.StickerCode = StickerCode;
    }

    public int getCategoryIdFk() {
        return categoryIdFk;
    }

    public void setCategoryIdFk(int categoryIdFk) {
        categoryIdFk = categoryIdFk;
    }

    public String getStickerCode() {
        return StickerCode;
    }

    public void setStickerCode(String stickerCode) {
        StickerCode = stickerCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
