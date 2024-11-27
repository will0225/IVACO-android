package com.example.myapplication.ui.measurements;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.UserMeasurement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Measurement_O2List_Activity extends AppCompatActivity {
    private static final String TAG = "MeasurementActivity";
    private String patientId;
    private String authToken;
    private static final String MEASUREMENT_TYPE = "SPO2"; // Updated filter type
    private TableLayout tableLayout; // Declare the TableLayout
    private List<UserMeasurement> measurementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_o2_list);

        tableLayout = findViewById(R.id.report_table); // Initialize the TableLayout
        measurementList = new ArrayList<>();

        fetchMeasurements();  // Call the method to fetch and log measurements

        // Retrieve the "type" and "measurement_id" from the intent
        String measurementType = getIntent().getStringExtra("measurement_type");
        String measurementId = getIntent().getStringExtra("measurement_id");

        // Log to verify the retrieved values
        Log.d("SPO2Activity", "Measurement Type: " + measurementType);
        Log.d("SPO2Activity", "Measurement Id: " + measurementId);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "TEMPERATURE";
                // Logic to open a new activity or dialog for adding a new measurement
                Intent intent = new Intent(Measurement_O2List_Activity.this, Measurement_O2_Activity.class);
                intent.putExtra("measurement_type", type);
                startActivity(intent);
            }
        });

        // Initialize the close button
        Button closeButton = findViewById(R.id.closeButton);

        // Set up the click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close this activity and go back to the previous one
                finish();
            }
        });
    }

    private void fetchMeasurements() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        patientId = sharedPreferences.getString("id", null);
        authToken = sharedPreferences.getString("token", null);

        if (patientId != null && authToken != null) {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<List<UserMeasurement>> call = apiService.getMeasurements(patientId, "Bearer " + authToken);

            call.enqueue(new Callback<List<UserMeasurement>>() {
                @Override
                public void onResponse(Call<List<UserMeasurement>> call, Response<List<UserMeasurement>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<UserMeasurement> measurements = response.body();
                        measurementList.clear(); // Clear existing data
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1); // Clear previous rows, keep header and placeholder

                        // Filter and add only SPO2 measurements to the list
                        boolean hasMeasurements = false; // Track if we have any measurements
                        for (UserMeasurement measurement : measurements) {
                            if (MEASUREMENT_TYPE.equals(measurement.getType())) {
                                measurementList.add(measurement);
                                addMeasurementRow(measurement); // Add row for each measurement
                                hasMeasurements = true; // Found at least one measurement
                                Log.d(TAG, "Added Measurement ID: " + measurement.getId());
                            }
                        }

                        // If no measurements are found, you can show a placeholder or handle it appropriately
                        if (!hasMeasurements) {
                            showPlaceholderRow();
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch measurements. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<UserMeasurement>> call, Throwable t) {
                    Log.e(TAG, "API Call failed: " + t.getMessage());
                }
            });
        } else {
            Log.e(TAG, "User ID or Token is null");
        }
    }

    // Method to add a row to the TableLayout
    private void addMeasurementRow(UserMeasurement measurement) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView titleTextView = new TextView(this);
        titleTextView.setText(measurement.getTitle()); // Assuming getTitle() returns the title
        titleTextView.setPadding(15, 15, 15, 15);
        titleTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        TextView valueTextView = new TextView(this);
        valueTextView.setText(String.valueOf(measurement.getValues())); // Assuming getValues() returns a numeric value
        valueTextView.setPadding(15, 15, 15, 15);
        valueTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        TextView statusTextView = new TextView(this);
        // Check if status is null; if so, display "Pending"
        String status = measurement.getStatus() != null ? measurement.getStatus() : "Pending";
        statusTextView.setText(status);
        statusTextView.setPadding(15, 15, 15, 15);
        statusTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));


        TextView dateTextView = new TextView(this);
        String formattedDate = measurement.getCreatedAt(); // Default to original in case parsing fails
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = originalFormat.parse(measurement.getCreatedAt());
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateTextView.setText(formattedDate);
        dateTextView.setPadding(15, 15, 15, 15);
        dateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        // Add TextViews to the TableRow
        row.addView(titleTextView);
        row.addView(valueTextView);
        row.addView(statusTextView);
        row.addView(dateTextView);

        // Add the row to the TableLayout
        tableLayout.addView(row);
    }

    // Show a placeholder row if no measurements are available
    private void showPlaceholderRow() {
        // Create a placeholder row
        TableRow placeholderRow = new TableRow(this);
        TextView placeholderTextView = new TextView(this);
        placeholderTextView.setText("No measurements available");
        placeholderTextView.setPadding(10, 10, 10, 10);
        placeholderTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Add placeholder TextView to the row and the row to the table
        placeholderRow.addView(placeholderTextView);
        tableLayout.addView(placeholderRow);
    }
}
