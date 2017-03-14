package com.sdsmdg.cognizance2017.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.Event;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context ctx;
    List<Event> eventsList;
    private boolean isDelete;
    private List<Event> favEvents = new ArrayList<Event>();
    private Realm realm;

    public RecyclerAdapter(Context ctx, List<Event> eventsList, boolean isDelete) {
        this.ctx = ctx;
        this.eventsList = eventsList;
        this.isDelete = isDelete;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView eventIcon;
        public TextView titleText, themeText, timeText;
        public CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventIcon = (ImageView) itemView.findViewById(R.id.event_icon);
            titleText = (TextView) itemView.findViewById(R.id.event_title);
            themeText = (TextView) itemView.findViewById(R.id.event_theme);
            timeText = (TextView) itemView.findViewById(R.id.event_time);
            if (isDelete) {
                checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
                checkBox.setVisibility(View.VISIBLE);
                this.setIsRecyclable(false);
            }
        }

        @Override
        public void onClick(View v) {

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
        final Event currentEvent = eventsList.get(position);
        holder.titleText.setText(currentEvent.getTitle());
        holder.themeText.setText(currentEvent.getTheme());
        holder.timeText.setText(currentEvent.getTime());
        //set Event Icon later
        //Drawable res = ContextCompat.getDrawable(ctx, currentEvent.getImageId());
        Drawable res = ContextCompat.getDrawable(ctx, R.drawable.ic_menu_send);
        holder.eventIcon.setImageDrawable(res);

        if (isDelete) {
            Realm.init(ctx);
            realm = Realm.getDefaultInstance();
            holder.checkBox.setChecked(currentEvent.isFav());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    realm.beginTransaction();
                    currentEvent.setFav(isChecked);
                    realm.commitTransaction();
                    /*if (isChecked) {
                        //favEvents.add(currentEvent);
                    } else {
                        //favEvents.remove(currentEvent);
                    }*/
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public List<Event> getFavEvents() {
        return favEvents;
    }
}
