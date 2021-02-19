package com.example.broadcastsandbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver br = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction("com.example.broadcastsandbox.TEST_NOTIFICATION");
        this.registerReceiver(br, filter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (br != null)
            unregisterReceiver(br);
    }

    public void onClick(View view){
        Intent intent = new Intent();
        intent.setAction("com.example.broadcastsandbox.TEST_NOTIFICATION");
        intent.putExtra("data", "Nothing to see here, move along.");
        sendBroadcast(intent);
    }

}