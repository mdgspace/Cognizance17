package com.sdsmdg.cognizance2017.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sdsmdg.cognizance2017.Fragments.AllEventsRecyclerFragment;

public class AllEventsVpagerAdapter extends FragmentPagerAdapter {

    private static int DAYS = 3;

    public AllEventsVpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will correspond to Day1
                return AllEventsRecyclerFragment.newInstance(0);
            case 1: // Fragment # 0 - This will correspond to Day2
                return AllEventsRecyclerFragment.newInstance(1);
            case 2: // Fragment # 1 - This will correspond to Day3
                return AllEventsRecyclerFragment.newInstance(2);
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
