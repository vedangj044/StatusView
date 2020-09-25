package com.vedangj044.statusview.Stickers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.vedangj044.statusview.Activity.GalleryViewFragment;
import com.vedangj044.statusview.R;

public class StickerListActivity extends FragmentActivity {

    private TextView allStickers;
    private TextView myStickers;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;
    private static final int NUM_PAGES = 2;


//    0 for allStickers
//    1 for myStickers
    int isActive = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_list);

        allStickers = findViewById(R.id.all_stickers);
        myStickers = findViewById(R.id.my_stickers);
        viewPager2 = findViewById(R.id.view_pager);

        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(isActive != position) {
                    toggle();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        allStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive == 1){
                    toggle();
                    viewPager2.setCurrentItem(0, true);
                }
            }
        });

        myStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive == 0){
                    toggle();
                    viewPager2.setCurrentItem(1, true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(viewPager2.getCurrentItem() == 0){
            super.onBackPressed();
        }
        else{
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            if(position == 0){
                return new AllStickerFragment();
            }
            else{
                return new MyStickersFragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    void toggle(){

        if(isActive == 0){
            isActive = 1;
            allStickers.setBackgroundResource(R.drawable.bottom_border_unselected);
            myStickers.setBackgroundResource(R.drawable.bottom_border_selected);
        }
        else{
            isActive = 0;
            allStickers.setBackgroundResource(R.drawable.bottom_border_selected);
            myStickers.setBackgroundResource(R.drawable.bottom_border_unselected);
        }

    }

}