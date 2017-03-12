package com.sdsmdg.cognizance2017.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.cognizance2017.adapters.AllEventsVpagerAdapter;
import com.sdsmdg.cognizance2017.R;

public class AllEventsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_events_viewpager, container, false);
        ViewPager vPager = (ViewPager) view.findViewById(R.id.all_events_vpager);
        AllEventsVpagerAdapter mAdapter = new AllEventsVpagerAdapter(getChildFragmentManager());
        vPager.setAdapter(mAdapter);
        TabLayout tabLayout= (TabLayout) view.findViewById(R.id.vpager_tabs);
        tabLayout.setupWithViewPager(vPager);
        return view;
    }
}