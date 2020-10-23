package com.vedangj044.statusview.Stickers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StickerImageDAO {

    @Query("Select StickerCode from StickerModel")
    List<String> getAllStickerId();

    @Query("Select url from stickermodel WHERE categoryIdFk == :id")
    List<String> getStickerURLById(int id);

    @Query("Select url from stickermodel where categoryIdFk == :id")
    LiveData<List<String>> getFullStickerByID(int id);

    @Insert
    void insertStickerImages(StickerModel stickerModel);

    @Update
    void updateStickerImages(StickerModel stickerImageModel);

    @Delete
    void deleteStickerImages(StickerModel stickerImageModel);

}
