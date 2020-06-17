package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private SecondViewModel viewModel;
    private ContentService service;
    private TextView textName;
    private Button btnSubmit;
    private EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        viewModel = new ViewModelProvider(this).get(SecondViewModel.class);

        textName = findViewById(R.id.textName);
        btnSubmit = findViewById(R.id.btnSubmit);
        editName = findViewById(R.id.editName);

        editName.setText(viewModel.getName().getValue());


        viewModel.getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editName.setText(viewModel.getName().getValue());
            }
        });

//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = editName.getText().toString();
//                service.asyncSendPerson(name);
//            }
//        });

        bindService(new Intent(SecondActivity.this, ContentService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((ContentService.LocalBinder) binder).getService();
            //将当前activity添加到接口集合中
            service.addCallback(callback);
            Log.d("SecondActivity", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("SecondActivity", "onServiceDisconnected");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        service.removeCallback(callback);
        Log.d("SecondActivity", "onDestroy");
    }

    private ContentService.Callback callback = new ContentService.Callback() {
        @Override
        public void getPerson(Person person) {
            textName.setText(person.getName());
            Log.d("SecondActivity", "person: " + person.getName());
        }
    };
}