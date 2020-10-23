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


    @Query("Select logo from tb_sticker_category")
    List<String> getStickerLogo();

    @Query("Select * from tb_sticker_category")
    LiveData<List<StickerCategoryModel>> getStickerCategory();


    @Insert
    void insertStickerCategory(StickerCategoryModel stickerCategoryModel);

    @Update
    void updateStickerCategory(StickerCategoryModel stickerCategoryModel);

    @Delete
    void deleteStickerCategory(StickerCategoryModel stickerCategoryModel);


}
