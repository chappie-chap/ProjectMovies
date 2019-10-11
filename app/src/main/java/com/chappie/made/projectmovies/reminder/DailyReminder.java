package com.chappie.made.projectmovies.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.chappie.made.projectmovies.MainActivity;
import com.chappie.made.projectmovies.R;

import java.util.Calendar;

public class DailyReminder extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 1;

    public DailyReminder(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notification(context);
    }

    private void notification(Context context) {
        String id = "channel_01";
        String name = "daily channel";
        String title = context.getString(R.string.daily_reminder);
        CharSequence msg = context.getString(R.string.daily_mgs);
        Intent intent = new Intent(context, MainActivity.class);
        int req=101;
        PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent)
                .getPendingIntent(req,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,id)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_notifications_active))
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(id, name,NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(id);
            if(manager!=null){
                manager.createNotificationChannel(channel);
            }
        }

        if(manager!=null){
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public void activateDaily(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void deactivatedDaily(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}