package com.example.admin.medicine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by NgocTri on 8/9/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FROM:" + remoteMessage.getFrom());

        //Check if the message contains data
        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        //Check if the message contains notification

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Dispay the notification
     * @param body
     */
    private void sendNotification(String body) {

        Intent intent = new Intent(this, welcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //Set sound of notification
        Uri notificationSound = Uri.parse("android.resource://com.example.admin.medicine/"+R.raw.whatsapp_whistle);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logoo)
                .setContentTitle("Saydaletk")
                .setContentText(body)
                .setAutoCancel(true);
                //.setSound(notificationSound)
                //.setContentIntent(pendingIntent);
        //.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        //.setVibrate(new long[] { 1000, 1000, 1000 , 1000, 1000 });

        Notification notification =notifiBuilder.build();
        //notification.sound=notificationSound;
        // notification.flags = Notification.FLAG_AUTO_CANCEL;
        // notification.flags = Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager)MyFirebaseMessagingService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notification);




    }
}
