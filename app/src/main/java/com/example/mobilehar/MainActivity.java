package com.example.mobilehar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobilehar.log.Logger;
import com.example.mobilehar.services.GravitySensorService;
import com.example.mobilehar.services.GyroscopeSensorService;
import com.example.mobilehar.har.Classifier;


public class MainActivity extends AppCompatActivity {
    private static final String MODEL_FILE = "file:///android_asset/frozen_model.pb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Classifier classifier = new Classifier(getAssets(), MODEL_FILE);
        float[] test = new float[2032 * 1 * 9];
        classifier.predict(test, 1, 2032, 9);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("MainActivity", "Start Service");
        startService(new Intent(this, GyroscopeSensorService.class));
        startService(new Intent(this, GravitySensorService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, GyroscopeSensorService.class));
        stopService(new Intent(this, GravitySensorService.class));
    }
}
