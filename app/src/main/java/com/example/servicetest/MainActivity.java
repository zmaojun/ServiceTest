package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MyService.DownloadBinder downloadBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startService = findViewById(R.id.start_serive);
        Button stopService = findViewById(R.id.stop_serive);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);

        Button bindService = findViewById(R.id.bind_serive);
        Button unbindService = findViewById(R.id.unbind_serive);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);

        Button intentService = findViewById(R.id.start_IntentSerive);
        intentService.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_serive:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_serive:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.bind_serive:
                Intent serviceIntent = new Intent(this, MyService.class);
                bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_serive:
                unbindService(serviceConnection);
                break;
            case R.id.start_IntentSerive:
                Intent intentService = new Intent(this, MyIntentService.class);
                startService(intentService);
                break;
            default:
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder)service;
            downloadBinder.startDownLoad();
            downloadBinder.getProgress();
            Log.d("MyService", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
