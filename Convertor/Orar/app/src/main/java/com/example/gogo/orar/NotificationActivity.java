package com.example.gogo.orar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;


public class NotificationActivity  extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repatingIntent = new Intent(context,AlarmReceiver.class);
        repatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,repatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String curs = intent.getExtras().getString("curs");
        String sala = intent.getExtras().getString("sala");
        String tip = intent.getExtras().getString("tip");
        Notification.Builder builder =  new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Orar")
                .setContentText("In 5 min incepe "+tip+" de "+curs+" in sala "+ sala).setAutoCancel(true);
        notificationManager.notify(100,builder.build());
    }
}
