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

    @Query("Select code from StickerModel where stickerCategoryId == :id")
    List<String> getAllStickerId(int id);

    @Query("Select image from stickermodel WHERE stickerCategoryId == :id")
    List<String> getStickerURLById(int id);

    @Query("Select image from stickermodel where stickerCategoryId == :id")
    LiveData<List<String>> getFullStickerByID(int id);

    @Query("SELECT COUNT(code) FROM stickermodel WHERE stickerCategoryId == :id")
    LiveData<Integer> getCountOfStickerByCategoryID(int id);

    @Insert
    void insertStickerImages(StickerModel stickerModel);

    @Update
    void updateStickerImages(StickerModel stickerImageModel);

    @Delete
    void deleteStickerImages(StickerModel stickerImageModel);

}
