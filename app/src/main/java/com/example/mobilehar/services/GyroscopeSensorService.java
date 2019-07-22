package com.example.mobilehar.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import com.example.mobilehar.sensor.GyroscopeSensorListener;
import com.example.mobilehar.thread.SensorThread;


public class GyroscopeSensorService extends Service {
    private SensorThread thread;


    @Override
    public void onCreate() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        GyroscopeSensorListener gyroscopeSensorListener = new GyroscopeSensorListener();
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        thread = new SensorThread(gyroscopeSensorListener, sensorManager, sensor);
        thread.start();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "GyroscopeSensorService destroying", Toast.LENGTH_SHORT).show();
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "GyroscopeSensorService starting", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

}
