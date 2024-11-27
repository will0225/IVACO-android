package com.example.myapplication.ui.measurement_reading;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.Measurement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodPressure_Reading_Activity extends AppCompatActivity {

    private String patientId;
    private EditText etSystolic, etDiastolic, etPulse, etTitle;
    private Button btnCancel, btnSubmit;
    private Spinner unitSpinner;
    private String mode = "METRIC"; // Default mode is "METRIC"
    private static final String TAG = "BloodPressureActivity"; // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_reading);

        Log.d(TAG, "Activity created");

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        patientId = sharedPreferences.getString("id", null);
        Log.d(TAG, "Patient ID retrieved: " + patientId);

        // Retrieve intent extras
        String readingSource = getIntent().getStringExtra("reading_source");
        String createdAt = getIntent().getStringExtra("created_at");
        String measurementType = getIntent().getStringExtra("measurement_type");
        String measurementId = getIntent().getStringExtra("measurement_id");

        Log.d(TAG, "Reading source: " + readingSource);
        Log.d(TAG, "Measurement type: " + measurementType);
        Log.d(TAG, "Measurement ID: " + measurementId);

        // Initialize UI components
        etSystolic = findViewById(R.id.et_systolic);
        etDiastolic = findViewById(R.id.et_diastolic);
        etPulse = findViewById(R.id.et_pulse);
        btnSubmit = findViewById(R.id.btn_submit); // Ensure this ID matches the layout
        btnCancel = findViewById(R.id.btn_cancel); // Ensure this ID matches the layout
        etTitle = findViewById(R.id.et_title);
        unitSpinner = findViewById(R.id.spinner_unit); // Assuming you have a dropdown for selecting unit

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the Cancel button action
        btnCancel.setOnClickListener(v -> {
            Log.d(TAG, "Cancel button clicked");
            finish(); // Close the current activity
        });

        // Set up the Submit button action
        btnSubmit.setOnClickListener(v -> {
            Log.d(TAG, "Submit button clicked");

            String systolic = etSystolic.getText().toString();
            String diastolic = etDiastolic.getText().toString();
            String pulse = etPulse.getText().toString();
            String title = etTitle.getText().toString();
            String unit = unitSpinner.getSelectedItem().toString(); // Assuming spinner holds unit selection

            // Check if fields are empty
            if (systolic.isEmpty() || diastolic.isEmpty() || pulse.isEmpty() || title.isEmpty()) {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Missing required fields: systolic=" + systolic + ", diastolic=" + diastolic + ", pulse=" + pulse + ", title=" + title);
                return;
            }

            // Convert blood pressure values to double
            try {
                double Systolic = Double.parseDouble(systolic);
                double Diastolic = Double.parseDouble(diastolic);
                double Pulse = Double.parseDouble(pulse);
                Log.d(TAG, "Parsed values: Systolic=" + Systolic + ", Diastolic=" + Diastolic + ", Pulse=" + Pulse);

                // Submit the measurement to the API
                sendMeasurement(title, Systolic, Diastolic, Pulse, unit, readingSource, measurementType, measurementId);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing numeric values", e);
                Toast.makeText(this, "Invalid numeric values entered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMeasurement(String title, double Systolic, double Diastolic, double Pulse, String unit,
                                 String readingSource, String measurementType, String measurementId) {

        // Retrieve token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Authorization token not found. Please log in again.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Authorization token not found");
            return;
        }

        // Create a list with the blood pressure values
        List<Double> values = new ArrayList<>();
        values.add(Systolic);   // Add systolic value
        values.add(Diastolic);  // Add diastolic value
        values.add(Pulse);      // Add pulse value
        Log.d(TAG, "Measurement values: " + values);

        // Create Measurement instance
        Measurement measurement = new Measurement(
                title,            // Title
                mode,             // Mode (e.g., "METRIC")
                measurementType,  // Type (e.g., "Blood Pressure")
                values,           // List of values
                unit,             // Unit (e.g., "mmHg" or "mmol/L")
                readingSource,    // Reading Source
                null,             // Doctor (assuming null is acceptable)
                null              // Nurse (assuming null is acceptable)
        );

        // Create the API call
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Measurement> call = apiService.sendMeasurement(patientId, "Bearer " + token, measurement);

        Log.d(TAG, "API call created: " + call.request().toString());

        call.enqueue(new Callback<Measurement>() {
            @Override
            public void onResponse(Call<Measurement> call, Response<Measurement> response) {
                // Log the request details
                Log.d(TAG, "Request: " + call.request().toString());

                if (response.isSuccessful()) {
                    // Log the successful response
                    Log.d(TAG, "Response Code: " + response.code());
                    Log.d(TAG, "Response Body: " + response.body());

                    Toast.makeText(BloodPressure_Reading_Activity.this, "Measurement submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after successful submission
                } else {
                    // Log the error response details
                    Log.e(TAG, "Error Code: " + response.code());
                    Log.e(TAG, "Error Message: " + response.message());
                    Log.e(TAG, "Error Body: " + response.errorBody());

                    Toast.makeText(BloodPressure_Reading_Activity.this, "Error submitting measurement: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Measurement> call, Throwable t) {
                // Log the failure
                Log.e(TAG, "Failure: " + t.getMessage());
                Toast.makeText(BloodPressure_Reading_Activity.this, "Failed to submit measurement: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
