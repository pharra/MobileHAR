package com.example.mobilehar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobilehar.log.Logger;
import com.example.mobilehar.services.GravitySensorService;
import com.example.mobilehar.services.GyroscopeSensorService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //stopService(new Intent(this, GyroscopeSensorService.class));
        //stopService(new Intent(this, GravitySensorService.class));
    }
}
