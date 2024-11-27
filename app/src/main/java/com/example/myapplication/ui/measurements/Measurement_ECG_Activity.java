package com.example.myapplication.ui.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.ui.measurement_reading.ECG_AutoReading_Activity;

public class Measurement_ECG_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        getWindow().setDecorFitsSystemWindows(false); // Enables edge-to-edge display

        setContentView(R.layout.activity_measurement_ecg);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize WebView
        WebView webView = findViewById(R.id.video_webview);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/deEiRCvekTU?si=CY211ZBkIPan87cC\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";

        // Load video into WebView
        webView.loadData(video, "text/html", "utf-8");

        // Enable JavaScript for the WebView
        webView.getSettings().setJavaScriptEnabled(true);

        // Set WebChromeClient to handle certain web events like loading YouTube videos
        webView.setWebChromeClient(new WebChromeClient());

        // Button: Automatically Add
        Button autoAddButton = findViewById(R.id.button_ecg_automatically_add);
        autoAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Measurement_ECG_Activity.this, "Auto Clicked", Toast.LENGTH_SHORT).show();
                // Navigate to AutomaticMeasurementActivity
                Intent intent = new Intent(Measurement_ECG_Activity.this, ECG_AutoReading_Activity.class);
                startActivity(intent);
            }
        });

    }
}
