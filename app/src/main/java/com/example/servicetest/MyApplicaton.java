package com.example.servicetest;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class MyApplicaton extends Application {
    private ContentService service;
    public MyApplicaton() {
//        getApplicationContext().bindService(new Intent(getApplicationContext(), ContentService.class), BIND_AUTO_CREATE);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
