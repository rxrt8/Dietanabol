package com.example.rxrt8.dietanabol;

import android.app.*;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;


/**
 * Created by rxrt8 on 2017-10-30.
 * Class create notification in Android at a specific time.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationsBaseManager notificationsBaseManager = new NotificationsBaseManager(context);
        Calendar calendar = java.util.Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        for (com.example.rxrt8.dietanabol.Notification n : notificationsBaseManager.giveAll()) {
            if (dayOfWeek == n.getDayAsInteger() && hour == n.getHour() && minute == n.getMinute()) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(n.getNotificationTitle())
                                .setContentText(n.getDay() + " " + n.getTime());
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                Intent resultIntent = new Intent(context, DietActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(DietActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());

            }

        }
    }
}
