package me.chrislewis.mentorship.models;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.chrislewis.mentorship.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static PendingIntent sender;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadCastReceiver", "Making notification");
        Toast.makeText(context,"Alarm received", Toast.LENGTH_SHORT).show();
        notificationStatus(context);
    }

    public static String convertDateTime(String dateTime) {
        if(dateTime.substring(0,1) == "0" && dateTime.substring(0,2) != "00") {
            return dateTime.substring(1);
        }
        return dateTime;
    }

    public static void setAlarm(Context context, String date, String time){
        Log.d("AlarmBroadcastReceiver","Alarm is setting!");
        Calendar cal = new GregorianCalendar();
        int year = Integer.valueOf(date.substring(0,4));
        int month = Integer.valueOf(convertDateTime(date.substring(5,7))) - 1;
        int day = Integer.valueOf(convertDateTime(date.substring(8,10)));
        int hour = Integer.valueOf(convertDateTime(time.substring(0,2)));
        int minute = Integer.valueOf(convertDateTime(time.substring(3,5)));
        Log.d("AlarmBroadcastReceiver", "year: " + Integer.toString(year));
        Log.d("AlarmBroadcastReceiver", "month: " + Integer.toString(month));
        Log.d("AlarmBroadcastReceiver", "day: " + Integer.toString(day));
        Log.d("AlarmBroadcastReceiver", "hour: " + Integer.toString(hour));
        Log.d("AlarmBroadcastReceiver", "minute: " + Integer.toString(minute));
        cal.set(year, month, day, hour, minute);
        //if neccessary for demo - send notification 15 minutes before actual
        //cal.add(Calendar.MINUTE, -15);
        Log.d("AlarmBroadcastReceiver", cal.getTime().toString());
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }

    private void notificationStatus(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "my_channel_id";
        CharSequence channelName = "My Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{1000, 2000});
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Upcoming event")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(channelId)
                .build();

        notificationManager.notify(0, notification);

    }
}
