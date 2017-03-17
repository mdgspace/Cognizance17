package com.sdsmdg.cognizance2017.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.activities.MainActivity;
import com.sdsmdg.cognizance2017.adapters.RecyclerAdapter;
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.models.EventList;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.sdsmdg.cognizance2017.activities.MainActivity.curDay;

public class AllEventsRecyclerFragment extends Fragment {

    private int choice;
    private int day;
    private Realm realm;
    private RealmResults<Event> results;
    private List<Event> result;
    private RecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Day Indicates which days events are to be shown and on which page of viewpager we are
            day = getArguments().getInt("Page Number", 10);
            choice = getArguments().getInt("choice", 10);
        } else {
            //error
            day = 10;
            choice = 10;
        }

        if (day == 5 && choice == -1) {
            //Enter favorite Event Selection Mode / Send whole list of events to adapter
            Realm.init(getActivity());
            realm = Realm.getDefaultInstance();
            results = realm.where(Event.class).findAll();
        } else if (choice == 50) {
            //Show Events marked as favorites
            Realm.init(getActivity());
            realm = Realm.getDefaultInstance();
            //Todo FIlTER QUERY ACCORDING TO DAY
            results = realm.where(Event.class).equalTo("fav", true).findAll();
        } else if (choice == 0) {
            //Show all events that are there
            Realm.init(getActivity());
            realm = Realm.getDefaultInstance();
            //Todo FIlTER QUERY ACCORDING TO DAY
            results = realm.where(Event.class).findAll();
        } else {
            //Display Events based on theme selected like robotics etc..
            Realm.init(getActivity());
            realm = Realm.getDefaultInstance();
            //Todo FIlTER QUERY ACCORDING TO DAY
            result = realm.where(EventList.class).equalTo("id", choice).findFirst().getEvents();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_events_recycler_view, container, false);
        RecyclerView eventsRecyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        if (day == 5 && choice == -1)
            adapter = new RecyclerAdapter(getActivity(), results, true);
        else if (choice == 50 || choice == 0)
            adapter = new RecyclerAdapter(getActivity(), results, false);
        else
            adapter = new RecyclerAdapter(getActivity(), result, false);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public static AllEventsRecyclerFragment newInstance(int page, int choice) {
        AllEventsRecyclerFragment fragment = new AllEventsRecyclerFragment();
        Bundle args = new Bundle();
        //page is 0 indexed, to get Day number add 1 to it
        args.putInt("Page Number", page);
        args.putInt("choice", choice);
        fragment.setArguments(args);
        return fragment;
    }
}
