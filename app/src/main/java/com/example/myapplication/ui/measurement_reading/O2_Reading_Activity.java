package com.example.myapplication.ui.measurement_reading;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.Measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class O2_Reading_Activity extends AppCompatActivity {

    private String patientId;
    private EditText etO2,etTitle;
    private Button btnCancel, btnSubmit;
    private Spinner unitSpinner;
    private String mode = "METRIC"; // Default mode is "METRIC"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_o2_reading);
        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        patientId = sharedPreferences.getString("id", null);

        // Retrieve intent extras
        String readingSource = getIntent().getStringExtra("reading_source");
        String createdAt = getIntent().getStringExtra("created_at");
        String measurementType = getIntent().getStringExtra("measurement_type");
        String measurementId = getIntent().getStringExtra("measurement_id");

        // Initialize UI components
        etO2 = findViewById(R.id.et_o2);
        etTitle = findViewById(R.id.et_title);
        unitSpinner = findViewById(R.id.spinner_unit); // Assuming you have a dropdown for selecting unit
        btnCancel = findViewById(R.id.btn_cancel);
        btnSubmit = findViewById(R.id.btn_submit);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the Cancel button action
        btnCancel.setOnClickListener(v -> {
            // Navigate back to the measurements screen
            finish(); // This will close the current activity
        });

        // Set up the Submit button action
        btnSubmit.setOnClickListener(v -> {
            String value = etO2.getText().toString();
            String title = etTitle.getText().toString();
            String unit = unitSpinner.getSelectedItem().toString(); // Assuming spinner holds unit selection

            // Check if fields are empty
            if (value.isEmpty() || title.isEmpty()) {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Convert blood glucose value to double
            double SPO2 = Double.parseDouble(value);
            // Submit the measurement to the API
            sendMeasurement( title, SPO2, unit, readingSource, measurementType, measurementId);
        });
    }

    private void sendMeasurement( String title, double SPO2, String unit,
                                  String readingSource, String measurementType, String measurementId) {

        // Retrieve token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Authorization token not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a list with the blood glucose value
        List<Double> values = new ArrayList<>();
        values.add(SPO2);  // Assuming you want to add only blood glucose to the list

        // Create Measurement instance
        Measurement measurement = new Measurement(
                title,            // Title
                mode,             // Mode (can be "METRIC" or whatever you defined)
                measurementType,  // Type (measurementType)
                Arrays.asList(Double.valueOf(SPO2)), // Convert to Double
                unit,             // Unit (e.g., "mmol/L" or "mg/dL")
                readingSource,    // Reading Source
                null,             // Doctor (assuming null is acceptable)
                null              // Nurse (assuming null is acceptable)
        );

        // Create the API call
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Measurement> call = apiService.sendMeasurement(patientId, "Bearer " + token, measurement);


        call.enqueue(new Callback<Measurement>() {
            @Override
            public void onResponse(Call<Measurement> call, Response<Measurement> response) {
                // Log the request details
                Log.d("SPO2Activity", "Request: " + call.request().toString());

                if (response.isSuccessful()) {
                    // Log the successful response
                    Log.d("SPO2Activity", "Response Code: " + response.code());
                    Log.d("SPO2Activity", "Response Body: " + response.body());

                    Toast.makeText(O2_Reading_Activity.this, "Measurement submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after successful submission
                } else {
                    // Log the error response details
                    Log.e("SPO2Activity", "Error Code: " + response.code());
                    Log.e("SPO2Activity", "Error Message: " + response.message());
                    Log.e("SPO2Activity", "Error Body: " + response.errorBody());

                    Toast.makeText(O2_Reading_Activity.this, "Error submitting measurement: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Measurement> call, Throwable t) {
                // Log the failure
                Log.e("SPO2Activity", "Failure: " + t.getMessage());
                Toast.makeText(O2_Reading_Activity.this, "Failed to submit measurement: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

