package com.sdsmdg.cognizance2017.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.adapters.RecyclerAdapter;
import com.sdsmdg.cognizance2017.models.EventModel;
import com.sdsmdg.cognizance2017.models.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class AllEventsRecyclerFragment extends Fragment {

    //    private int choice;
    private int day;
    private String title;
    private Realm realm;
    private RealmResults<EventModel> results;
    private RecyclerAdapter adapter;
    private List<String> mDeptList;
    private List<Type> mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Day Indicates which days events are to be shown and on which page of viewpager we are
            day = getArguments().getInt("day", 1);
            title = getArguments().getString("title", title);
        } else {
            day = 1;
            title = "Title";
        }
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        if (title.equals("Home")) {
            results = realm.where(EventModel.class).notEqualTo("day" + day, "").findAll();
        } else if (title.equals("Workshop") || title.equals("Mainstay")
                || title.equals("E-Summit") || title.equals("Project M.A.R.S")) {
            results = realm.where(EventModel.class).equalTo("type.category", title).findAll();
        } else if (title.equals("Favorites")) {
            results = realm.where(EventModel.class).equalTo("isFav", true).findAll();
        } else if (title.equals("DepartmentList")) {
            mDeptList = new ArrayList<String>();
            mType = realm.where(Type.class).equalTo("category", "Departmental").findAll();
            for (int i = 0; i < mType.size(); i++) {
                mDeptList.add(mType.get(i).getName());
            }
            //to remove duplicate elts.
            Set<String> hs = new HashSet<>();
            hs.addAll(mDeptList);
            mDeptList.clear();
            mDeptList.addAll(hs);
            Collections.sort(mDeptList);
        } else {
            results = realm.where(EventModel.class).equalTo("type.name", title).findAll();
        }
        //Toast.makeText(getContext(), "" + results.size(), Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_events_recycler_view, container, false);

        RecyclerView eventsRecyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        if (title.equals("Favorites")) {
            adapter = new RecyclerAdapter(getActivity(), results, true);
        } else if (title.equals("DepartmentList")) {
            adapter = new RecyclerAdapter(getActivity(), mDeptList);
        } else {
            adapter = new RecyclerAdapter(getActivity(), results, false);
        }
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setItemAnimator(new FadeInUpAnimator());
        eventsRecyclerView.getItemAnimator().setRemoveDuration(500);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setNestedScrollingEnabled(true);
        return view;
    }

    public static AllEventsRecyclerFragment newInstance(int day, String title) {
        AllEventsRecyclerFragment fragment = new AllEventsRecyclerFragment();
        Bundle args = new Bundle();
        args.putInt("day", day);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
}
