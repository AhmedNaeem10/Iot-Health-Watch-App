package com.example.iotwatch;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AzureIoTHubClient {
    // Replace with your device connection string values
    private static final String IOT_HUB_HOSTNAME = "ahmed-iot-hub.azure-devices.net";
    private static final String DEVICE_ID = "ahmed-device-1";
    private static final String DEVICE_KEY = "zTYPaN6ET9+r9+sURVATusoWp9+rdAFhd/0ridhLAzY=";

    public static void sendHeartRateToIoTHub(float heartRate) {
        new Thread(() -> {
            try {
                String resourceUri = IOT_HUB_HOSTNAME + "/devices/" + DEVICE_ID;
                System.out.println("AzureIoTHub" + "Generating SAS token for: " + resourceUri);

                String sasToken = SasGenerator.generateSasToken(resourceUri, DEVICE_KEY, 3600);
                System.out.println("AzureIoTHub" + "SAS Token: " + sasToken);

                URL url = new URL("https://" + IOT_HUB_HOSTNAME + "/devices/" + DEVICE_ID + "/messages/events?api-version=2020-09-30");
                System.out.println("AzureIoTHub" + "Sending telemetry to URL: " + url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", sasToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("timestamp", System.currentTimeMillis());
                payload.put("heartRate", heartRate);

                System.out.println("AzureIoTHub" + "Payload: " + payload.toString());

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                System.out.println("AzureIoTHub" + "Response code: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("AzureIoTHub" + "Error sending telemetry" + e.getMessage());
            }
        }).start();
    }
}
