package com.example.servicetest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class ContentService extends Service {
    private final int[] n = {1};
    private List<Callback> list;
    private Thread thread;
    private boolean isActive;

    public ContentService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand :" + intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        String name = intent.getStringExtra("NAME");
        Log.d(TAG, "Name=" + name + " onBind :" + intent);
        return new LocalBinder();
    }

    public final class LocalBinder extends Binder {
        public ContentService getService() {
            return ContentService.this;
        }
    }

    public interface Callback {
        public void getPerson(Person person);
    }

    public void addCallback(Callback callback) {
        list.add(callback);
    }

    public void removeCallback(Callback callback) {
        list.remove(callback);
    }

    public void Start() {
        if (thread != null)
            return;
        isActive = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int m = n[0]++;
                    handler.sendMessage(handler.obtainMessage(0, String.valueOf(m)));
                    sendContentBroadcast(String.valueOf(m));
                }
                Log.d(TAG, "thread id:" + thread.getId());
            }
        });
        thread.start();
    }

    public void Stop() {
        isActive = false;
    }

    private static final String TAG = "ContentService";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            String name = (String) msg.obj;
            Log.d(TAG, "name :" + name);
            Person person = new Person();
            person.setName(name);

            Log.i("ContentService", "---list.size()-->" + list.size());
            Log.i("ContentService", "---person-->" + person.getName());
            //遍历集合，通知所有的实现类，即activity
            for (int i = 0; i < list.size(); i++) {
                list.get(i).getPerson(person);
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void sendContentBroadcast(String name) {
        Intent intent = new Intent();
        intent.setAction("com.content");
        intent.putExtra("NAME", name);
        sendBroadcast(intent);
    }
}
