package com.vedangj044.statusview.Stickers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StickerImageDAO {



    @Insert
    void insertStickerImages(StickerImageModel stickerImageModel);

    @Update
    void updateStickerImages(StickerImageModel stickerImageModel);

    @Delete
    void deleteStickerImages(StickerImageModel stickerImageModel);

}
