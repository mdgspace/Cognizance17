package com.sdsmdg.cognizance2017.adapters;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.FavReceiver;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.activities.MainActivity;
import com.sdsmdg.cognizance2017.models.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

import static android.content.Context.ALARM_SERVICE;

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
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            this.setIsRecyclable(false);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((MainActivity)ctx).showSingleEventFragment();
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
        Drawable res = ContextCompat.getDrawable(ctx, R.drawable.ic_menu_send);
        holder.eventIcon.setImageDrawable(res);

            holder.checkBox.setChecked(currentEvent.isFav());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Realm.init(ctx);
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    currentEvent.setFav(isChecked);
                    realm.commitTransaction();
                    if(isChecked){
                        Calendar calendar = currentEvent.getNotificationTime();
                        //createNotification(calendar.getTimeInMillis());
                        createNotification(System.currentTimeMillis());
                    }
                    else {
                        cancelNotification(0);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public List<Event> getFavEvents() {
        return favEvents;
    }


    private void createNotification(long time) {
        Calendar calendar  = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,47);
        Intent intent = new Intent(ctx, FavReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 12, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(ctx, "" +  time,Toast.LENGTH_LONG).show();
    }
    private void cancelNotification(int id) {

        Intent intent = new Intent(ctx, FavReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 12, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(ctx, "Alarm has been cancelled" +  System.currentTimeMillis()+60*1000,Toast.LENGTH_LONG).show();
    }
}
