package com.example.mobilehar.thread;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

public class SensorThread extends Thread {
    private SensorEventListener sensorEventListener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private static final int UPTATE_INTERVAL_TIME = 20000;

    public SensorThread(SensorEventListener sensorEventListener, SensorManager sensorManager, Sensor sensor) {
        super();
        this.sensorEventListener = sensorEventListener;
        this.sensorManager = sensorManager;
        this.sensor = sensor;
    }

    @Override
    public void run() {
        Looper.prepare();
        Handler handler = new Handler();
        sensorManager.registerListener(sensorEventListener, sensor, UPTATE_INTERVAL_TIME, handler );
        Looper.loop();
    }

    @Override
    public void interrupt() {
        sensorManager.unregisterListener(sensorEventListener);
        super.interrupt();
    }
}
