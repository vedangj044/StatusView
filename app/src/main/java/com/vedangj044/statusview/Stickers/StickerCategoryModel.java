package com.vedangj044.statusview.Stickers;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tb_sticker_category")
public class StickerCategoryModel {

    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "logo")
    private String logo;

    @ColumnInfo(name = "status")
    private int status;

    public StickerCategoryModel(int id, String name, String logo, int status) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.status = status;
    }

    @Ignore
    private List<StickerModel> images = new ArrayList<>();

    @Ignore
    public StickerCategoryModel(String title, String logo, List<StickerModel> images) {
        this.name = title;
        this.logo = logo;
        this.images = images;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    @Ignore
    public void setImages(List<StickerModel> images) {
        this.images = images;
    }

    @Ignore
    public List<StickerModel> getImages() {
        if(images == null){
            return new ArrayList<>();
        }
        return images;
    }

    @Override
    public String toString() {
        return "StickerCategoryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", status=" + status +
                '}';
    }
}
