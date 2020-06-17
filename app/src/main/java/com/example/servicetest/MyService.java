package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private Notification notification;
    private DownloadBinder mBinder = new DownloadBinder();
    public MyService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        notification = new Notification.Builder(this)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setChannelId("channelId")
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager = (NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("channelId", "notificationName", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("MyService", "onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "onDestroy");
        super.onDestroy();
    }

    class DownloadBinder extends Binder {
        public void startDownLoad() {
            Log.d("MyService", "startDownLoad");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress");
            return 0;
        }

        public void setNotfication(String message) {
//            notification.
        }

    }
}
