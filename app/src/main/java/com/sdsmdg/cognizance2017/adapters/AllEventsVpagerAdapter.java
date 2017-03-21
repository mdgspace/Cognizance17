package com.sdsmdg.cognizance2017.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sdsmdg.cognizance2017.fragments.AllEventsRecyclerFragment;

public class AllEventsVpagerAdapter extends FragmentPagerAdapter {

    private static int DAYS = 3;
    private String title;

    public AllEventsVpagerAdapter(FragmentManager fm, String title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will correspond to Day1
                return AllEventsRecyclerFragment.newInstance(0, title);
            case 1: // Fragment # 1 - This will correspond to Day2
                return AllEventsRecyclerFragment.newInstance(1, title);
            case 2: // Fragment # 2 - This will correspond to Day3
                return AllEventsRecyclerFragment.newInstance(2, title);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return DAYS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Day " + (position + 1);
    }
}
