package com.example.myapplication.ui.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.ui.measurement_reading.RespiratoryRate_Reading_Activity;

public class Measurement_RespiratoryRate_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_measurement_respiratory_rate);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize WebView
        WebView webView = findViewById(R.id.video_webview);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/atm-gnobU7o?si=ml7yfuJd2ps-8PXo\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";

        // Load video into WebView
        webView.loadData(video, "text/html", "utf-8");

        // Enable JavaScript for the WebView
        webView.getSettings().setJavaScriptEnabled(true);

        // Set WebChromeClient to handle certain web events like loading YouTube videos
        webView.setWebChromeClient(new WebChromeClient());

//        // Button: Automatically Add
//        Button autoAddButton = findViewById(R.id.button_rr_automatically_add);
//        autoAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Measurement_RespiratoryRate_Activity.this, "Auto Clicked", Toast.LENGTH_SHORT).show();
//                // Uncomment to navigate to AutomaticMeasurementActivity
//                // Intent intent = new Intent(Measurement_BloodPressure_Activity.this, AutomaticMeasurementActivity.class);
//                // startActivity(intent);
//            }
//        });

        // Button: Manually Add
        Button manualAddButton = findViewById(R.id.button_rr_manually_add);
        manualAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Measurement_RespiratoryRate_Activity.this, "Manually Clicked", Toast.LENGTH_SHORT).show();
                // Navigate to BloodPressure_Reading_Activity
                Intent intent = new Intent(Measurement_RespiratoryRate_Activity.this, RespiratoryRate_Reading_Activity.class);
                startActivity(intent);
            }
        });
    }
}
