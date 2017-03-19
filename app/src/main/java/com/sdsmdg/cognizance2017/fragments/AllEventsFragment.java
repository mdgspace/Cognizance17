package com.sdsmdg.cognizance2017.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.adapters.AllEventsVpagerAdapter;

import static com.sdsmdg.cognizance2017.activities.MainActivity.curDay;

public class AllEventsFragment extends Fragment {

    private int choice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            choice = getArguments().getInt("Choice", 10);
        else
            choice = 100;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_events_viewpager, container, false);
        ViewPager vPager = (ViewPager) view.findViewById(R.id.all_events_vpager);
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curDay = position + 24;
                Toast.makeText(getContext(), "" + curDay, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        AllEventsVpagerAdapter mAdapter = new AllEventsVpagerAdapter(getChildFragmentManager(), choice);
        vPager.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.vpager_tabs);
        if (tabLayout != null)
            tabLayout.setupWithViewPager(vPager);

        return view;
    }

    public static AllEventsFragment newInstance(int choice) {
        AllEventsFragment fragment = new AllEventsFragment();
        Bundle args = new Bundle();
        //choice refers to which kind of events the pager must display e.g.- add_fav, theme events, robotics etc.
        args.putInt("Choice", choice);
        fragment.setArguments(args);
        return fragment;
    }
}