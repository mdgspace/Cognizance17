package com.sdsmdg.cognizance2017;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandedListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> daysList;
    private ArrayList<Event> day1Events,day2Events,day3Events;
    private HashMap<String, ArrayList<Event>> dayWiseEventList;
    public ExpandedListAdapter(Context context, List<String> listDataHeader,
                     HashMap<String, ArrayList<Event>> listChildData) {
        this._context = context;
        this.daysList = listDataHeader;
        this.dayWiseEventList = listChildData;
        day1Events = new ArrayList<>();
        for(int i=0; i<3;i++){
            Event event = new Event();
            event.setContinuous(true);
            event.setDescription("Event Description");
            event.setEndDay(28);
            event.setEndMinute(00);
            event.setImageId(R.mipmap.ic_launcher);
            event.setLocation("Roorkee");
            event.setStartDay(25);
            event.setStartHour(8);
            event.setStartMinute(00);
            event.setTheme("Theme");
            event.setTitle("Title");
            day1Events.add(event);
        }
        day2Events = day1Events;
        day3Events = day1Events;
        daysList  = new ArrayList<>();
        daysList.add("DAY 1");
        daysList.add("DAY 2");
        daysList.add("DAY 3");
        dayWiseEventList = new HashMap<>();
        dayWiseEventList.put(daysList.get(0),day1Events);
        dayWiseEventList.put(daysList.get(1),day2Events);
        dayWiseEventList.put(daysList.get(2),day3Events);
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
            LayoutInflater inflater = (LayoutInflater) this._context
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
            LayoutInflater inflater = (LayoutInflater) this._context
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
