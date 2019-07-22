package com.example.mobilehar.entity;

import java.util.HashMap;

public class MessageEvent {

    public static int GYROSCOPE = 0;

    public static int ACCELEROMETER = 1;

    public int type;

    public float x;

    public float y;

    public float z;

    public long timeInterval;

    public MessageEvent(int type, float x, float y, float z, long timeInterval) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.timeInterval = timeInterval;
    }
}
