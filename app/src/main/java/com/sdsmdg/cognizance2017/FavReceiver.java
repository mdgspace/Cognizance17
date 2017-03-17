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

import com.sdsmdg.cognizance2017.activities.MainActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Arihant Jain on 3/17/2017.
 */

public class FavReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.putExtra("message","hello everyone");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_ONE_SHOT);
        //  TaskStackBuilder task = TaskStackBuilder.create(this);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle ("Sample Notification")
                .setContentText ("Hello World! This is my first notification!")
                .setSmallIcon (R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        Notification.BigTextStyle textStyle = new Notification.BigTextStyle();
        String longTextMessage = "I went up one pair of stairs.";
        longTextMessage += " / Just like me. ";
//...
        textStyle.bigText(longTextMessage);

// Set the summary text:
        textStyle.setSummaryText ("The summary text goes here.");

// Plug this style into the builder:
        builder.setStyle (textStyle);
        ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).notify(12,builder.build());
    }
}
