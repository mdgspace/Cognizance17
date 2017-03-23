package com.sdsmdg.cognizance2017.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.activities.MainActivity;
import com.sdsmdg.cognizance2017.models.EventModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.sdsmdg.cognizance2017.activities.MainActivity.curDay;
import static com.sdsmdg.cognizance2017.activities.MainActivity.mainAct;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context ctx;
    private boolean isInFav;
    private List<EventModel> normalEventList;
    private List<String> deptList;
    private Realm realm;
    private AdapterListener mListener;
    private int day;

    public interface AdapterListener {
        public void itemsRemoved();
    }

    public void setmListener(AdapterListener mListener) {
        this.mListener = mListener;
    }

    public RecyclerAdapter(Context ctx, List<String> deptList) {
        this.ctx = ctx;
        this.deptList = deptList;
    }

    public RecyclerAdapter(Context ctx, List<EventModel> eventsList, boolean isInFav, int day) {
        this.ctx = ctx;
        this.isInFav = isInFav;
        this.day = day;
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
            String time = "";
            if (curDay == 24) {
                time = currentEvent.getDay1();
            } else if (curDay == 25) {
                time = currentEvent.getDay2();
            } else {
                time = currentEvent.getDay3();
            }
            try {
                if (!time.equals("")) {
                    String hr = time.substring(0, 2);
                    String min = time.substring(2, 4);

                    String hr2 = time.substring(5, 7);
                    String min2 = time.substring(7, 9);
                    String modifiedTime = hr + ":" + min + " - " + hr2 + ":" + min2;
                    holder.timeText.setText(modifiedTime);
                }
            } catch (Exception e) {
                //Toast.makeText(ctx,""+currentEvent.getId(),Toast.LENGTH_SHORT).show();
            }
            if (currentEvent.getVenue().equals("")) {
                holder.locationText.setText("Venue");
            } else {
                holder.locationText.setText(currentEvent.getVenue());
            }
            if ((day == 1 && currentEvent.isFav1()) || (day == 2 && currentEvent.isFav2()) || (day == 3 && currentEvent.isFav3())) {
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
            if (day == 1)
                holder.checkBox.setChecked(currentEvent.isFav1());
            else if (day == 2)
                holder.checkBox.setChecked(currentEvent.isFav2());
            else if (day == 3)
                holder.checkBox.setChecked(currentEvent.isFav3());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Realm.init(ctx);
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    EventModel eventModel = realm.where(EventModel.class).equalTo("id", currentEvent.getId()).findFirst();
                    if (day == 1)
                        eventModel.setFav1(isChecked);
                    else if (day == 2)
                        eventModel.setFav2(isChecked);
                    else if (day == 3)
                        eventModel.setFav3(isChecked);
                    realm.commitTransaction();
                    if (day == 1)
                        currentEvent.setFav1(isChecked);
                    else if (day == 2)
                        currentEvent.setFav2(isChecked);
                    else if (day == 3)
                        currentEvent.setFav3(isChecked);
                    if (isChecked) {
                        ((MainActivity) mainAct).createNotification(currentEvent,day+23);
                        ((MainActivity) mainAct).showSnack();
                        holder.locationText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                        holder.timeText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                        holder.titleText.setTextColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                        holder.clockIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimarySelected));
                        holder.markerIcon.setColorFilter(ctx.getResources().getColor(R.color.colorPrimarySelected));
                        holder.divider.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimarySelected));
                    } else {
                        ((MainActivity) mainAct).cancelNotification(currentEvent.getId(),day+23);
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
        } else {
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
        if (normalEventList.isEmpty()) {
            if (mListener != null)
                mListener.itemsRemoved();
        }
    }
}
