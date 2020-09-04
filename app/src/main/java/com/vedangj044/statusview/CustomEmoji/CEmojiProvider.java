package com.vedangj044.statusview.CustomEmoji;

import androidx.annotation.NonNull;

import com.vanniktech.emoji.EmojiProvider;
import com.vanniktech.emoji.emoji.EmojiCategory;
import com.vanniktech.emoji.google.category.ActivitiesCategory;
import com.vanniktech.emoji.google.category.AnimalsAndNatureCategory;
import com.vanniktech.emoji.google.category.FlagsCategory;
import com.vanniktech.emoji.google.category.FoodAndDrinkCategory;
import com.vanniktech.emoji.google.category.ObjectsCategory;
import com.vanniktech.emoji.google.category.SmileysAndPeopleCategory;
import com.vanniktech.emoji.google.category.SymbolsCategory;
import com.vanniktech.emoji.google.category.TravelAndPlacesCategory;


// This is google emoji providers class only change is the addition of the custom emoji category
// Reference - https://github.com/vanniktech/Emoji/blob/master/emoji-google/src/main/java/com/vanniktech/emoji/google/GoogleEmojiProvider.java

public class CEmojiProvider implements EmojiProvider {
    @NonNull
    @Override
    public EmojiCategory[] getCategories() {
        return new EmojiCategory[]{
                new SmileysAndPeopleCategory(),
                new AnimalsAndNatureCategory(),
                new FoodAndDrinkCategory(),
                new ActivitiesCategory(),
                new TravelAndPlacesCategory(),
                new ObjectsCategory(),
                new SymbolsCategory(),
                new FlagsCategory(),

                // The custom emoji category
                new CEmojiList()
        };
    }
}