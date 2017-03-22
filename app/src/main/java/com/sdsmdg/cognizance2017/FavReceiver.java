package com.sdsmdg.cognizance2017;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.activities.EventDescriptionActivity;
import com.sdsmdg.cognizance2017.activities.MainActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Arihant Jain on 3/17/2017.
 */

public class FavReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getBooleanExtra("cancel",false)){
            ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).cancel(intent.getIntExtra("id",0));
        }
        else {
            Intent mainIntent = new Intent(context,EventDescriptionActivity.class);
            mainIntent.putExtra("id",intent.getIntExtra("realId",6));
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,mainIntent,PendingIntent.FLAG_ONE_SHOT);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle ("Cognizance")
                    .setContentText (intent.getStringExtra("title")+" is about to start")
                    .setSmallIcon (R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).notify(intent.getIntExtra("id",0),builder.build());

        }}
}
