package com.sdsmdg.cognizance2017.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.models.EventList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ExpandedListAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private List<String> daysList;
    private ArrayList<Event> day1Events,day2Events,day3Events;
    private Realm realm;
    private RealmResults<EventList> results;
    private HashMap<String,RealmList<Event>> dayWiseEventList;
    public ExpandedListAdapter(Context context) {
        this.ctx = context;
        Realm.init(ctx);
        realm = Realm.getDefaultInstance();
        results = realm.where(EventList.class).findAll();
        day1Events = new ArrayList<>();
        day2Events = day1Events;
        day3Events = day1Events;
        daysList  = new ArrayList<>();
        daysList.add("DAY 1");
        daysList.add("DAY 2");
        daysList.add("DAY 3");
        dayWiseEventList = new HashMap<>();
        dayWiseEventList.put(daysList.get(0),results.get(0).getEvents());
        dayWiseEventList.put(daysList.get(1),results.get(1).getEvents());
        dayWiseEventList.put(daysList.get(2),results.get(2).getEvents());
    }

    @Override
    public int getGroupCount() {
        return this.daysList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.dayWiseEventList.get(this.daysList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.daysList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dayWiseEventList.get(this.daysList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_layout, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.group_title);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Event event = (Event) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item_layout, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.event_title);
        TextView theme = (TextView) convertView.findViewById(R.id.event_theme);
        TextView time = (TextView) convertView.findViewById(R.id.event_time);
        ImageView icon = (ImageView) convertView.findViewById(R.id.event_icon);

        title.setText(event.getTitle());
        theme.setText(event.getTheme());
        time.setText(event.getTime());
        icon.setImageResource(R.drawable.ic_menu_gallery);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
