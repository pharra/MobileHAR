package com.example.mobilehar.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.mobilehar.entity.MessageEvent;
import com.example.mobilehar.log.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;


public class GyroscopeSensorListener implements SensorEventListener {

    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();

        long timeInterval = currentUpdateTime - lastUpdateTime;

        lastUpdateTime = currentUpdateTime;
        float[] values = event.values;

        // 获得x,y,z加速度
        float x = values[0];
        float y = values[1];
        float z = values[2];

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;

        EventBus.getDefault().post(new MessageEvent(MessageEvent.GYROSCOPE, lastX, lastY, lastZ, timeInterval));

        //Logger.d("GyroscopeSensorListener", String.format(Locale.SIMPLIFIED_CHINESE, "gyroscope sensor: %d %f %f %f", timeInterval, lastX, lastY, lastZ));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
