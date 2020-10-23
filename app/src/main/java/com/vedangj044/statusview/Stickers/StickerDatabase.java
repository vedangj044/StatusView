package com.vedangj044.statusview.Stickers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {StickerCategoryModel.class, StickerModel.class}, exportSchema = false, version = 1)
public abstract class StickerDatabase extends RoomDatabase {

    private static final String DB_NAME = "sticker_db";
    private static StickerDatabase instance;

    public static synchronized StickerDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), StickerDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract StickerCategoryDAO stickerCategoryDAO();

    public abstract StickerImageDAO stickerImageDAO();
}
