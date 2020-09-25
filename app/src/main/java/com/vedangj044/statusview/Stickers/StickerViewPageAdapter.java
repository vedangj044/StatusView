package com.vedangj044.statusview.Stickers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StickerViewPageAdapter extends FragmentPagerAdapter {

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    public StickerViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public StickerViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
}
