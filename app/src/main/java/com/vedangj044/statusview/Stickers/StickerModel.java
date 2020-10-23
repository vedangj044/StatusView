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
    @ColumnInfo(name = "StickerId")
    @NonNull
    private String StickerId;

    @ColumnInfo(name = "url")
    private String url;

    public StickerModel(String StickerId, int categoryIdFk, String url) {
        this.categoryIdFk = categoryIdFk;
        this.url = url;
        this.StickerId = StickerId;
    }

    public int getCategoryIdFk() {
        return categoryIdFk;
    }

    public void setCategoryIdFk(int categoryIdFk) {
        categoryIdFk = categoryIdFk;
    }

    public String getStickerId() {
        return StickerId;
    }

    public void setStickerId(String stickerId) {
        StickerId = stickerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
