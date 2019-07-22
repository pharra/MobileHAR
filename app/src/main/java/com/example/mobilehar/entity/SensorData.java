package com.example.mobilehar.entity;

import java.util.ArrayList;
import java.util.List;

public class SensorData {
    public List<Float> x;
    public List<Float> y;
    public List<Float> z;

    public SensorData() {
        x = new ArrayList<>();
        y = new ArrayList<>();
        z = new ArrayList<>();
    }
}
