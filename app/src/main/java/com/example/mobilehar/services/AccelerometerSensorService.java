package com.example.mobilehar.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import com.example.mobilehar.sensor.AccelerometerSensorListener;


public class AccelerometerSensorService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        private AccelerometerSensorListener accelerometerSensorListener;
        private SensorManager sensorManager;
        private static final int UPTATE_INTERVAL_TIME = 20000;

        public ServiceHandler(Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {
            if (accelerometerSensorListener == null) {
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                accelerometerSensorListener = new AccelerometerSensorListener();
                Sensor sensor = sensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (null != sensor)
                    sensorManager.registerListener(accelerometerSensorListener, sensor, UPTATE_INTERVAL_TIME, this);
            }
        }

        public void stop() {
            if (accelerometerSensorListener != null) {
                sensorManager.unregisterListener(accelerometerSensorListener);
            }
            sensorManager = null;
            accelerometerSensorListener = null;
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "AccelerometerSensorService destroying", Toast.LENGTH_SHORT).show();
        mServiceHandler.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "AccelerometerSensorService starting", Toast.LENGTH_SHORT).show();
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

}
