package com.example.mobilehar.eventbus;

import java.util.HashMap;

public class MessageEvent {

    public static int GYROSCOPE = 0;

    public static int ACCELEROMETER = 1;

    public int type;

    public HashMap<String, Float> data;

    public MessageEvent(HashMap<String, Float> data, int type) {
        this.data = data;
        this.type = type;
    }


}
