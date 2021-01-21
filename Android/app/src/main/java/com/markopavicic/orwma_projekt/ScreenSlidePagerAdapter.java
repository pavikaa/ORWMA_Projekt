package com.markopavicic.orwma_projekt;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;
    private static final String BASE_NAME = "#%d";

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return StatsFragment.newInstance();
            default:
                return AddGameDataFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "Stats";
        else
            return "Add game data";
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
