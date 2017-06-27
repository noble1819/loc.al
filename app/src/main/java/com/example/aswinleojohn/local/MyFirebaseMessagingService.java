package com.example.aswinleojohn.local;




import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SimpleDateFormat dateFormat;
    String receivedmsg;
    String senderid;
    String formattedDate;
    String datamsg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        dateFormat = new SimpleDateFormat("HH:mm:ss aa");
        formattedDate = dateFormat.format(new Date()).toString();
        //receivedmsg=remoteMessage.getNotification().getBody();
        senderid=remoteMessage.getData().get("username");
        datamsg=remoteMessage.getData().get("datamessage");
        pref = getSharedPreferences(senderid+"_message", MODE_PRIVATE);
        editor = pref.edit();
        Log.e(TAG, "Notification Message Body: " + receivedmsg);
        editor.putString(formattedDate+"_R",datamsg);
        editor.commit();
        //Calling method to generate notification
        // app is in foreground, broadcast the push message
        Intent pushNotification = new Intent("pushNotification");
        pushNotification.putExtra("message", datamsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        sendNotification(datamsg);


    }
    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, Message.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Log.e(TAG, "Notification Message Body: " + messageBody);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(senderid)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }
}