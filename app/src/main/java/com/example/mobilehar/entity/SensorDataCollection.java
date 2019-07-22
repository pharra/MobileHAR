package com.example.mobilehar.entity;

public class SensorDataCollection {
    public SensorData accelerometer;
    public SensorData gyroscope;

    public SensorDataCollection(SensorData accelerometer, SensorData gyroscope) {
        this.accelerometer = accelerometer;
        this.gyroscope = gyroscope;
    }
}
