package com.markopavicic.orwma_projekt;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.Locale;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 1;
    private static final String BASE_NAME = "#%d";

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return AddGameDataFragment.newInstance();
            //case 1:
            //return AddGameDataFragment.newInstance();
            //case 3:
            //return ScreenSlidePageFragmentModular.newInstance();
            //default:
            //return ScreenSlidePageFragment.newInstance("Hello world!");
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(Locale.getDefault(), BASE_NAME, position + 1);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
