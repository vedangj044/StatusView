//package com.vedangj044.statusview.Stickers;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.PrimaryKey;
//
//import static androidx.room.ForeignKey.CASCADE;
//
//@Entity(tableName = "StickerImageModel", foreignKeys = {@ForeignKey(entity = AllStickerModel.class,
//parentColumns = "id",
//childColumns = "StickerPackId",
//onDelete = CASCADE)})
//public class StickerImageModel {
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    @ColumnInfo(name = "StickerPackId")
//    private int StickerPackId;
//
//    @ColumnInfo(name = "url")
//    private String url;
//
//    public StickerImageModel(int StickerPackId, String url) {
//        this.url = url;
//        this.StickerPackId = StickerPackId;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getStickerPackId() {
//        return StickerPackId;
//    }
//
//    public void setStickerPackId(int stickerPackId) {
//        StickerPackId = stickerPackId;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//}
