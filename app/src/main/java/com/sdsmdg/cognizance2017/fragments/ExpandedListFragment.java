package com.sdsmdg.cognizance2017.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.sdsmdg.cognizance2017.adapters.ExpandedListAdapter;
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandedListFragment extends Fragment {
    ExpandedListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Event>> listDataChild;
    public ExpandedListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expanded_list, container, false);

        expListView = (ExpandableListView) view.findViewById(R.id.expanded_list);
        listAdapter = new ExpandedListAdapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        return view;
    }
}
