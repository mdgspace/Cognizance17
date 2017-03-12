package com.sdsmdg.cognizance2017.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.cognizance2017.adapters.RecyclerAdapter;
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.EventsData;
import com.sdsmdg.cognizance2017.R;

import java.util.ArrayList;
import java.util.List;

public class AllEventsRecyclerFragment extends Fragment {

    private static int position;
    private int day;
    public static List<Event> dummyEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day=getArguments().getInt("Page Number", 10);

        dummyEvents = new ArrayList<Event>();

        for (int i = 0; i < 13; i++) {
            dummyEvents.add(new Event(EventsData.titles[i],
                    EventsData.themes[i],
                    EventsData.location[i],
                    EventsData.desc[i],
                    EventsData.startDay[i],
                    EventsData.startHr[i],
                    EventsData.startMin[i],
                    R.drawable.ic_menu_camera,
                    EventsData.endDay[i],
                    EventsData.endHr[i],
                    EventsData.endMin[i],
                    true));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_events_recycler_view, container, false);
        RecyclerView eventsRecyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(getActivity(), dummyEvents);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public static AllEventsRecyclerFragment newInstance(int page) {
        AllEventsRecyclerFragment fragment = new AllEventsRecyclerFragment();
        Bundle args = new Bundle();
        //page is 0 indexed
        args.putInt("Page Number", page);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setData(int pos) {
        position = pos;
        //change addapter data
    }
}
