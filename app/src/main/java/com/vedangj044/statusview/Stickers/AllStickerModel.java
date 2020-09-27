package com.vedangj044.statusview.Stickers;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "StickerCategory")
public class AllStickerModel {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "logo")
    private String logo;

    @ColumnInfo(name = "status")
    private int status;

    public AllStickerModel(int id, String name, String logo, int status) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.status = status;
    }

    @Ignore
    private List<String> images = new ArrayList<>();

    @Ignore
    public AllStickerModel(String title, String logo, List<String> images) {
        this.name = title;
        this.logo = logo;
        this.images = images;
    }


    public int getId() {
        return id;
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
    public void setImages(List<String> images) {
        this.images = images;
    }

    @Ignore
    public List<String> getImages() {
        if(images == null){
            return new ArrayList<String>();
        }
        return images;
    }

}
