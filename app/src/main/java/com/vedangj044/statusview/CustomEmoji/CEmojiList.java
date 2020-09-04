package com.vedangj044.statusview.CustomEmoji;

import androidx.annotation.NonNull;

import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.emoji.EmojiCategory;
import com.vedangj044.statusview.R;

public class CEmojiList implements EmojiCategory {


    @NonNull
    @Override
    public Emoji[] getEmojis() {
        return new Emoji[]{
                /*
                * List of custom emoji
                *
                * codePoint -> represents the emoji as unicode starts with 0xC----
                * String array -> name of the emoji in a string list
                * Drawable resource
                * isDuplicate -> false
                *
                * */

                new Emoji(0xCFFFF, new String[]{"custom_smiley"}, R.drawable.ic_emoji_foreground, false),
                new Emoji(0xCF333, new String[]{"custom_keyboard_black"}, R.drawable.ic_keyboard_foreground, false)
        };
    }

    @Override
    public int getIcon() {

        // Category icon of this custom emoji category
        return R.drawable.ic_account_foreground;
    }

    @Override
    public int getCategoryName() {

        // String resource representing the name of the custom emoji class
        return R.string.CustomEmojiCategoryName;
    }
}
