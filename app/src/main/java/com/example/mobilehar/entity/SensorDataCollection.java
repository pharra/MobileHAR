package com.example.mobilehar.entity;

public class SensorDataCollection {
    public SensorData accelerometer;
    public SensorData gyroscope;
    public int label;

    public SensorDataCollection(SensorData accelerometer, SensorData gyroscope, int label) {
        this.accelerometer = accelerometer;
        this.gyroscope = gyroscope;
        this.label = label;
    }
}
