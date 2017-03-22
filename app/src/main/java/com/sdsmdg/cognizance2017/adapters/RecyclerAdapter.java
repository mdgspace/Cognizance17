package com.sdsmdg.cognizance2017.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.activities.MainActivity;
import com.sdsmdg.cognizance2017.models.EventModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

import static com.sdsmdg.cognizance2017.activities.MainActivity.mainAct;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context ctx;
    private List<EventModel> eventsList;
    private boolean isInFav;
    private List<EventModel> normalEventList;
    private List<String> deptList;
    private Realm realm;

    public RecyclerAdapter(Context ctx, List<String> deptList) {
        this.ctx = ctx;
        this.deptList = deptList;
    }

    public RecyclerAdapter(Context ctx, List<EventModel> eventsList, boolean isInFav) {
        this.ctx = ctx;
        this.eventsList = eventsList;
        this.isInFav = isInFav;
        normalEventList = new ArrayList<EventModel>();
        try {
            realm = Realm.getDefaultInstance();
            normalEventList.addAll(realm.copyFromRealm(eventsList));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView clockIcon, markerIcon;
        public TextView titleText, locationText, timeText;
        public ToggleButton checkBox;
        View divider;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (deptList == null) {
                titleText = (TextView) itemView.findViewById(R.id.event_title);
                locationText = (TextView) itemView.findViewById(R.id.event_location);
                timeText = (TextView) itemView.findViewById(R.id.event_time);
                checkBox = (ToggleButton) itemView.findViewById(R.id.toggle);
                clockIcon = (ImageView) itemView.findViewById(R.id.clock);
                markerIcon = (ImageView) itemView.findViewById(R.id.marker);
                divider = itemView.findViewById(R.id.divider);
            } else {
                titleText = (TextView) itemView.findViewById(R.id.department_name);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (deptList == null)
                ((MainActivity) ctx).showEvent(normalEventList.get(getAdapterPosition()).getId());
            else {
                ((MainActivity) ctx).isOnDeptViewPagerFragment = true;
                ((MainActivity) ctx).showEvents(deptList.get(getAdapterPosition()),
                        deptList.get(getAdapterPosition()));
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (deptList == null)
            itemView = LayoutInflater.from(ctx).inflate(R.layout.event_item_layout, parent, false);
        else
            itemView = LayoutInflater.from(ctx).inflate(R.layout.department_list_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (deptList == null) {
            final EventModel currentEvent = normalEventList.get(position);
            holder.titleText.setText(currentEvent.getName());
            if (currentEvent.getTime().equals(""))
                holder.timeText.setText("Time");
            else {
                holder.timeText.setText(currentEvent.getTime());
            }
            if (currentEvent.getVenue().equals("")) {
                holder.locationText.setText("Venue");
            } else {
                holder.locationText.setText(currentEvent.getVenue());
            }
            if (currentEvent.isFav()) {
                holder.locationText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                holder.timeText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                holder.titleText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                holder.clockIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimarySelected));
                holder.markerIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimarySelected));
                holder.divider.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
            } else {
                holder.locationText.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.timeText.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.titleText.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.clockIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimary));
                holder.markerIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimary));
                holder.divider.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));

            }
        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(currentEvent.isFav());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Realm.init(ctx);
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                EventModel eventModel = realm.where(EventModel.class).equalTo("id", currentEvent.getId()).findFirst();
                eventModel.setFav(isChecked);
                realm.commitTransaction();
                currentEvent.setFav(isChecked);
                if (isChecked) {
                    if(!(currentEvent.getTime().equals("") || currentEvent.getDate().equals(""))){
                        int hr = Integer.parseInt(currentEvent.getTime().substring(0,2));
                        int min = Integer.parseInt(currentEvent.getTime().substring(2,4));
                        int day = Integer.parseInt(currentEvent.getDate().substring(0,2));
                        Calendar calender = Calendar.getInstance();
                        calender.set(Calendar.MONTH,Calendar.MARCH);
                        calender.set(Calendar.YEAR,2017);
                        calender.set(Calendar.DAY_OF_MONTH,day);
                        calender.set(Calendar.HOUR_OF_DAY,hr);
                        calender.set(Calendar.MINUTE,min);
                        if(System.currentTimeMillis()<calender.getTimeInMillis())
                            ((MainActivity)mainAct).createNotification(calender,currentEvent);
                        else {
                            Toast.makeText(ctx, "This event has already started", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ctx, "can't set alarm ,date is null", Toast.LENGTH_SHORT).show();
                    }
                    holder.locationText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                    holder.timeText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                    holder.titleText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                    holder.clockIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimarySelected));
                    holder.markerIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimarySelected));
                    holder.divider.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                } else {
                    if(!currentEvent.getDate().equals("")) {
                        int day = Integer.parseInt(currentEvent.getDate().substring(0, 2));
                        ((MainActivity)mainAct).cancelNotification(Integer.parseInt(day + "" + currentEvent.getId()));
                    }
                    if (isInFav) {
                        deleteFromFav(holder.getAdapterPosition());
                    } else {
                        holder.locationText.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                        holder.timeText.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                        holder.titleText.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                        holder.clockIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimary));
                        holder.markerIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimary));
                        holder.divider.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
        });
        }
        else {
            if (!deptList.get(position).equals(""))
                holder.titleText.setText(deptList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (deptList == null)
            return normalEventList.size();
        else
            return deptList.size();
    }

    private void deleteFromFav(int index) {
        normalEventList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, normalEventList.size());
    }
}
