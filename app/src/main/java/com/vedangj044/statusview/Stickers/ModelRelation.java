package com.vedangj044.statusview.Stickers;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ModelRelation {
    @Embedded public AllStickerModel allStickerModel;
    @Relation(
            parentColumn = "id",
            entityColumn = "StickerPackId"
    )
    public List<StickerImageModel> imageModels;

}
