package com.guoxiaoxing.android.framework.demo.application.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.guoxiaoxing.android.framework.demo.R;

public class ClientActivity extends AppCompatActivity implements ICounterCallback {

    private TextView mTvCounter;

    private ICounterService counterService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            counterService = ((CounterService.CounterBinder) service).getCounterService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            counterService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = new Intent(ClientActivity.this, CounterService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        mTvCounter = (TextView) findViewById(R.id.tv_counter);

        findViewById(R.id.btn_start_counter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterService != null) {
                    counterService.startCounter(0, ClientActivity.this);
                }
            }
        });

        findViewById(R.id.btn_stop_counter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterService.stopCounter();
            }
        });

    }

    @Override
    public void count(int count) {
        mTvCounter.setText(String.valueOf(count));
    }
}
