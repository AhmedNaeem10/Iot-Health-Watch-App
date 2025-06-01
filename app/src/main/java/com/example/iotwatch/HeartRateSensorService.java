package com.example.iotwatch;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

public class HeartRateSensorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private Handler handler = new Handler();
    private float lastHeartRate = -1f;

    private Runnable readSensorTask = new Runnable() {
        @Override
        public void run() {
            sensorManager.registerListener(HeartRateSensorService.this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            handler.postDelayed(this, 60 * 1000); // every 1 minute
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        handler.post(readSensorTask);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(readSensorTask);
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            lastHeartRate = event.values[0];
            AzureIoTHubClient.sendHeartRateToIoTHub(lastHeartRate);
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
