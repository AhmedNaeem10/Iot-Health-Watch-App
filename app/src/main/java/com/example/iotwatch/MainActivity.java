package com.example.iotwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.heart_rate_text);
        tv.setText("Heart Rate Monitoring Started");

//        AzureIoTHubClient.sendHeartRateToIoTHub(65);
        // Start the sensor service
        Intent serviceIntent = new Intent(this, HeartRateSensorService.class);
        startService(serviceIntent);
    }
}