package com.example.myapplication.ui.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.ui.measurement_reading.BloodGlucose_Reading_Activity;
import com.example.myapplication.ui.measurement_reading.BloodPressure_Reading_Activity;

public class Measurement_BloodPressure_Activity extends AppCompatActivity {

    private String readingSource;  // To store whether auto or manual reading is selected
    private String createdAt;      // To store the current timestamp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_measurement_blood_pressure);

        // Retrieve the "type" and "measurement_id" from the intent
        String measurementType = getIntent().getStringExtra("measurement_type");
        String measurementId = getIntent().getStringExtra("measurement_id");

        Log.d("BloodGlucoseActivity", "Measurement Type: " + measurementType);
        Log.d("BloodGlucoseActivity", "Measurement Id: " + measurementId);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize WebView
        WebView webView = findViewById(R.id.video_webview);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/kSpl8sJVN3k?si=EtgnwpIRvfNzeZuL\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";

        // Load video into WebView
        webView.loadData(video, "text/html", "utf-8");

        // Enable JavaScript for the WebView
        webView.getSettings().setJavaScriptEnabled(true);

        // Set WebChromeClient to handle certain web events like loading YouTube videos
        webView.setWebChromeClient(new WebChromeClient());

        // Button: Automatically Add
//        Button autoAddButton = findViewById(R.id.button_bp_automatically_add);
//        autoAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Measurement_BloodPressure_Activity.this, "Auto Clicked", Toast.LENGTH_SHORT).show();
//                readingSource = "AUTO";  // Set reading source to AUTO
//                navigateToNextScreen();
//            }
//        });
        // Button: Manually Add
        Button manualAddButton = findViewById(R.id.button_bp_manually_add);
        manualAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Measurement_BloodPressure_Activity.this, "Manually Clicked", Toast.LENGTH_SHORT).show();
                readingSource = "MANUAL";  // Set reading source to MANUAL
               navigateToNextScreen();
            }
        });
    }
    // Method to navigate to the next screen with the necessary data
    private void navigateToNextScreen() {
        Intent intent = new Intent(Measurement_BloodPressure_Activity.this, BloodPressure_Reading_Activity.class);
        intent.putExtra("reading_source", readingSource);  // Pass reading source
        intent.putExtra("measurement_type", getIntent().getStringExtra("measurement_type"));  // Pass the measurement type
        intent.putExtra("measurement_id", getIntent().getStringExtra("measurement_id"));  // Pass the measurement ID
        startActivity(intent);
    }
}
