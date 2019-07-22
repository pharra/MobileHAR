package com.example.mobilehar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mobilehar.entity.MessageEvent;
import com.example.mobilehar.entity.SensorData;
import com.example.mobilehar.entity.SensorDataCollection;
import com.example.mobilehar.log.Logger;
import com.example.mobilehar.services.AccelerometerSensorService;
import com.example.mobilehar.services.GyroscopeSensorService;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private static final String MODEL_FILE = "file:///android_asset/frozen_model.pb";


    private SensorData accelerometerData;
    private SensorData gyroscopeData;

    private TextView acc_x = null;
    private TextView acc_y = null;
    private TextView acc_z = null;

    private TextView gyro_x = null;
    private TextView gyro_y = null;
    private TextView gyro_z = null;

    private TextView predictTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acc_x = findViewById(R.id.acc_x);
        acc_y = findViewById(R.id.acc_y);
        acc_z = findViewById(R.id.acc_z);

        gyro_x = findViewById(R.id.gyro_x);
        gyro_y = findViewById(R.id.gyro_y);
        gyro_z = findViewById(R.id.gyro_z);

        predictTextView = findViewById(R.id.predictTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("MainActivity", "Start Service");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void processData(MessageEvent message) {
        final float x = message.x;
        final float y = message.y;
        final float z = message.z;
        if (message.type == MessageEvent.ACCELEROMETER) {
            accelerometerData.x.add(x);
            accelerometerData.y.add(y);
            accelerometerData.z.add(z);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    acc_x.setText(String.valueOf(x));
                    acc_y.setText(String.valueOf(y));
                    acc_z.setText(String.valueOf(z));
                }
            });
        } else {
            gyroscopeData.x.add(x);
            gyroscopeData.y.add(y);
            gyroscopeData.z.add(z);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gyro_x.setText(String.valueOf(x));
                    gyro_y.setText(String.valueOf(y));
                    gyro_z.setText(String.valueOf(z));
                }
            });
        }
    }


    public void startClick(View view) {
        accelerometerData = new SensorData();
        gyroscopeData = new SensorData();

        EventBus.getDefault().register(this);
        startService(new Intent(this, GyroscopeSensorService.class));
        startService(new Intent(this, AccelerometerSensorService.class));
    }

    public void stopClick(View view) {
        stopService(new Intent(this, GyroscopeSensorService.class));
        stopService(new Intent(this, AccelerometerSensorService.class));
        EventBus.getDefault().unregister(this);
        Logger.d("Post accelerometerData length: ", String.valueOf(accelerometerData.x.size()));
        Logger.d("Post gyroscopeData length: ", String.valueOf(gyroscopeData.x.size()));
        Gson gson = new Gson();
        String data = gson.toJson(new SensorDataCollection(accelerometerData, gyroscopeData));
        new PredTask().execute(data);
    }


    class PredTask extends AsyncTask<String, Void, String> {

        private String url = "http://192.168.31.187:5001/pred";

        @Override
        protected String doInBackground(String... json) {
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(json[0], JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            final String res = s;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    predictTextView.setText(res);
                }
            });
        }
    }
}
