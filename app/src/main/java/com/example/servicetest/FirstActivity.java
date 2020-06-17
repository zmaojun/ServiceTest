package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.servicetest.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity{
    private FirstViewModel viewModel;
    private ActivityFirstBinding binding;
    private ContentService service;
    private ContentReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FirstViewModel.class);

        Intent intent = new Intent(FirstActivity.this, ContentService.class);
        intent.putExtra("NAME", "Hello");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        binding.btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, SecondActivity.class));
            }
        });
        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.Stop();
                service.stopSelf();
                finish();
            }
        });

        binding.textView.setText(viewModel.getName().getValue());

        viewModel.getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.textView.setText(viewModel.getName().getValue());
            }
        });

        doRegisterReceiver();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((ContentService.LocalBinder)binder).getService();
//            service.addCallback(callback);
            service.Start();
            Log.d("FirstActivity", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("FirstActivity", "onServiceDisconnected");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.removeCallback(callback);
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
        Log.d("FirstActivity", "onDestroy");
    }

    ContentService.Callback callback = new ContentService.Callback() {
        @Override
        public void getPerson(Person person) {
            viewModel.SetName(person.getName());
            Log.d("FirstActivity", "person: " + person.getName());
        }
    };

    private void doRegisterReceiver() {
        receiver = new ContentReceiver();
        IntentFilter filter = new IntentFilter("com.content");
        registerReceiver(receiver, filter);
    }

    public class ContentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("NAME");
            Person person = new Person();
            person.setName(name);
            viewModel.SetName(name);
        }
    }

}