package com.sdsmdg.cognizance2017;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context ctx;
    List<Event> eventsList = new ArrayList<Event>();

    public RecyclerAdapter(Context ctx, List<Event> eventsList) {
        this.ctx = ctx;
        this.eventsList = eventsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView eventIcon;
        public TextView titleText, themeText, timeText;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventIcon = (ImageView) itemView.findViewById(R.id.event_icon);
            titleText = (TextView) itemView.findViewById(R.id.event_title);
            themeText = (TextView) itemView.findViewById(R.id.event_theme);
            timeText = (TextView) itemView.findViewById(R.id.event_time);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(ctx).inflate(R.layout.event_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.titleText.setText(currentEvent.getTitle());
        holder.themeText.setText(currentEvent.getTheme());
        String min1, min2;
        min1 = currentEvent.getStartMinute() / 10 == 0 ? "0" + currentEvent.getStartMinute() : "" + currentEvent.getStartMinute();
        min2 = currentEvent.getEndMinute() / 10 == 0 ? "0" + currentEvent.getEndMinute() : "" + currentEvent.getEndMinute();
        holder.timeText.setText(currentEvent.getTime());
        //set Event Icon later
        Drawable res = ContextCompat.getDrawable(ctx, currentEvent.getImageId());
        holder.eventIcon.setImageDrawable(res);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

}
