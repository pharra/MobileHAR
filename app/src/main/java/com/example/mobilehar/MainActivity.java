package com.example.mobilehar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mobilehar.eventbus.MessageEvent;
import com.example.mobilehar.log.Logger;
import com.example.mobilehar.services.AccelerometerSensorService;
import com.example.mobilehar.services.GyroscopeSensorService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {
    private static final String MODEL_FILE = "file:///android_asset/frozen_model.pb";


    private Hashtable<String, ArrayList<Float>> accelerometerData;
    private Hashtable<String, ArrayList<Float>> gyroscopeData;

    private TextView acc_x = findViewById(R.id.acc_x);
    private TextView acc_y = findViewById(R.id.acc_y);
    private TextView acc_z = findViewById(R.id.acc_z);

    private TextView gyro_x = findViewById(R.id.gyro_x);
    private TextView gyro_y = findViewById(R.id.gyro_y);
    private TextView gyro_z = findViewById(R.id.gyro_z);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("MainActivity", "Start Service");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void processData(MessageEvent message) {
        final float x = message.data.get("x");
        final float y = message.data.get("x");
        final float z = message.data.get("x");
        if (message.type == MessageEvent.ACCELEROMETER) {
            accelerometerData.get("x").add(x);
            accelerometerData.get("y").add(y);
            accelerometerData.get("z").add(z);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    acc_x.setText(String.valueOf(x));
                    acc_y.setText(String.valueOf(y));
                    acc_z.setText(String.valueOf(z));
                }
            });
        } else {
            gyroscopeData.get("x").add(x);
            gyroscopeData.get("y").add(y);
            gyroscopeData.get("z").add(z);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gyro_x.setText(String.valueOf(x));
                    gyro_y.setText(String.valueOf(y));
                    gyro_z.setText(String.valueOf(z));
                }
            });
        }
    }


    public void startClick(View view) {
        accelerometerData = new Hashtable<String, ArrayList<Float>>();
        gyroscopeData = new Hashtable<String, ArrayList<Float>>();
        accelerometerData.put("x", new ArrayList<Float>());
        accelerometerData.put("y", new ArrayList<Float>());
        accelerometerData.put("z", new ArrayList<Float>());
        gyroscopeData.put("x", new ArrayList<Float>());
        gyroscopeData.put("y", new ArrayList<Float>());
        gyroscopeData.put("z", new ArrayList<Float>());
        EventBus.getDefault().register(this);
        startService(new Intent(this, GyroscopeSensorService.class));
        startService(new Intent(this, AccelerometerSensorService.class));
    }

    public void stopClick(View view) {
        stopService(new Intent(this, GyroscopeSensorService.class));
        stopService(new Intent(this, AccelerometerSensorService.class));
        EventBus.getDefault().unregister(this);

    }

}
