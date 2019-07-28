package com.example.mobilehar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehar.entity.CollectData;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private String HOST = "http://192.168.15.107:5001/";

    private static final String[] LABELS = {
            "WALKING",
            "WALKING_UPSTAIRS",
            "WALKING_DOWNSTAIRS",
            "SITTING",
            "STANDING",
            "LAYING",
            "STAND_TO_SIT",
            "SIT_TO_STAND",
            "SIT_TO_LIE",
            "LIE_TO_SIT",
            "STAND_TO_LIE",
            "LIE_TO_STAND"
    };

    private SensorData accelerometerData;
    private SensorData gyroscopeData;

    private TextView acc_x = null;
    private TextView acc_y = null;
    private TextView acc_z = null;

    private TextView gyro_x = null;
    private TextView gyro_y = null;
    private TextView gyro_z = null;

    private TextView predictTextView = null;
    private EditText timerEditText = null;

    private long timer = 5000;

    private int label = 1;

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

        Spinner spinner = (Spinner) findViewById(R.id.labelSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                MainActivity.this.label = pos + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        timerEditText = findViewById(R.id.timerEditText);


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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    timer = Long.valueOf(timerEditText.getText().toString()) * 1000;
                    Thread.sleep(2000);
                    Vibrator vib = (Vibrator) MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                    vib.vibrate(1000);
                    start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(timer);
                                pred();
                                Vibrator vib = (Vibrator) MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                                vib.vibrate(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void collectClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    timer = Long.valueOf(timerEditText.getText().toString()) * 1000;
                    Thread.sleep(2000);
                    Vibrator vib = (Vibrator) MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                    vib.vibrate(1000);
                    start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(timer);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-HH-mm-ss");
                                String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                                date += "-" + String.valueOf(timer);
                                collect(date);
                                Vibrator vib = (Vibrator) MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                                vib.vibrate(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void start() {
        accelerometerData = new SensorData();
        gyroscopeData = new SensorData();
        EventBus.getDefault().register(this);
        startService(new Intent(this, GyroscopeSensorService.class));
        startService(new Intent(this, AccelerometerSensorService.class));
    }

    public void pred() {
        stopService(new Intent(this, GyroscopeSensorService.class));
        stopService(new Intent(this, AccelerometerSensorService.class));
        EventBus.getDefault().unregister(this);
        Logger.d("Post accelerometerData length: ", String.valueOf(accelerometerData.x.size()));
        Logger.d("Post gyroscopeData length: ", String.valueOf(gyroscopeData.x.size()));
        Gson gson = new Gson();
        String data = gson.toJson(new SensorDataCollection(accelerometerData, gyroscopeData, -1));
        new PredTask().execute(data);
    }

    public void collect(String date) {
        stopService(new Intent(this, GyroscopeSensorService.class));
        stopService(new Intent(this, AccelerometerSensorService.class));
        EventBus.getDefault().unregister(this);
        Logger.d("Post accelerometerData length: ", String.valueOf(accelerometerData.x.size()));
        Logger.d("Post gyroscopeData length: ", String.valueOf(gyroscopeData.x.size()));
        Gson gson = new Gson();
        String data = gson.toJson(new SensorDataCollection(accelerometerData, gyroscopeData, label));
        date = LABELS[label - 1] + "-" + label + "-" + date;
        storeData(date, data);
    }

    public void storeData(String fileName, String data) {
        String allFile = "allFile";
        try {
            FileOutputStream fos = openFileOutput(allFile, Context.MODE_APPEND);
            fos.write(fileName.getBytes());
            fos.write("\n".getBytes());
            fos.close();
            FileOutputStream file = openFileOutput(fileName, Context.MODE_PRIVATE);
            file.write(data.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadClick(View view) {
        String allFile = "allFile";
        try {
            FileInputStream fos = openFileInput(allFile);
            FileOutputStream f_dele;

            StringBuilder files = new StringBuilder();
            byte[] buffer = new byte[1024];
            int size;
            while ((size = fos.read(buffer)) > 0) {
                files.append(new String(buffer, 0, size));
            }
            fos.close();
            deleteFile(allFile);
            String[] file = files.toString().split("\n");
            CollectData collectData = new CollectData();
            for (String f : file) {
                fos = openFileInput(f);

                StringBuilder s = new StringBuilder();
                while ((size = fos.read(buffer)) > 0) {
                    s.append(new String(buffer, 0, size));
                }
                collectData.files.put(f, s.toString());
                fos.close();
                deleteFile(f);
            }
            Gson gson = new Gson();
            new UploadTask().execute(gson.toJson(collectData).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class PredTask extends AsyncTask<String, Void, String> {

        private String url = HOST + "pred";

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

    class UploadTask extends AsyncTask<String, Void, String> {

        private String url = HOST + "upload";

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
