package com.vedangj044.statusview.Stickers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StickerCategoryDAO {

    @Transaction
    @Query("Select * from stickercategory WHERE id == :id")
    List<ModelRelation> getStickerImagesURL(int id);

    @Query("Select logo from stickercategory")
    List<String> getStickerLogo();

    @Query("Select * from StickerCategory")
    LiveData<List<AllStickerModel>> getStickerCategory();

    @Query("Select id from StickerCategory")
    List<Integer> getAllStickerId();

    @Insert
    void insertStickerCategory(AllStickerModel allStickerModel);

    @Update
    void updateStickerCategory(AllStickerModel allStickerModel);

    @Delete
    void deleteStickerCategory(AllStickerModel allStickerModel);


}