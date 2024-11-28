package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.Measurement;
import com.example.myapplication.ui.measurements.Measurement_BloodGlucoseList_Activity;
import com.example.myapplication.ui.measurements.Measurement_BloodGlucose_Activity;
import com.example.myapplication.ui.measurements.Measurement_BloodPressureList_Activity;
import com.example.myapplication.ui.measurements.Measurement_BloodPressure_Activity;
import com.example.myapplication.ui.measurements.Measurement_ECG_Activity;
import com.example.myapplication.ui.measurements.Measurement_O2List_Activity;
import com.example.myapplication.ui.measurements.Measurement_O2_Activity;
import com.example.myapplication.ui.measurements.Measurement_RespiratoryRateList_Activity;
import com.example.myapplication.ui.measurements.Measurement_RespiratoryRate_Activity;
import com.example.myapplication.ui.measurements.Measurement_TempList_Activity;
import com.example.myapplication.ui.measurements.Measurement_Temp_Activity;
import com.google.android.material.navigation.NavigationView;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.UUID;  // Import this at the top of your file


public class MeasurementActivity extends AppCompatActivity {

    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;
    // Define variables for token and user ID
    private String authToken;
    private String patientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_measurement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Retrieve user ID and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        patientId = sharedPreferences.getString("id", null);
        authToken = sharedPreferences.getString("token", null);

        // Log the retrieved values
        if (authToken != null && patientId != null) {
            Log.d("MeasurementActivity", "Token: " + authToken);
            Log.d("MeasurementActivity", "Patient ID: " + patientId);
        } else {
            Log.d("MeasurementActivity", "No token or user ID found");
        }

        // Find buttons
//        ImageButton buttonBack = findViewById(R.id.buttonBack);

        // Set click listener for the back button
//        buttonBack.setOnClickListener(v -> onBackPressed());
        // Drawer and navigation setup
//        main = findViewById(R.id.main);
//        buttonDrawer = findViewById(R.id.buttonDrawer);
//        navigationView = findViewById(R.id.navigationView);
//        buttonDrawer.setOnClickListener(view -> main.open());
//        navigationView.setNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.navProfile) {
//                Toast.makeText(MeasurementActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MeasurementActivity.this, ProfileActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            else if (item.getItemId() == R.id.navReports) {
//                Toast.makeText(MeasurementActivity.this, "Reports Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MeasurementActivity.this, ReportActivity.class);
//                startActivity(intent);
//            }
//            else if (item.getItemId() == R.id.navTeleMed) {
//                Toast.makeText(MeasurementActivity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MeasurementActivity.this, TeleMedActivity.class);
//                startActivity(intent);
//            }
//            else if (item.getItemId() == R.id.navDevice) {
//                Toast.makeText(MeasurementActivity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MeasurementActivity.this, Device_Activity.class);
//                startActivity(intent);
//            }
//            else if (item.getItemId() == R.id.navLogout) {
//                // Handle logout logic here
//                Toast.makeText(MeasurementActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
//                logout();
//            }
//            // Close the drawer after an item is clicked
//            main.closeDrawer(GravityCompat.START);
//            return true; // Return true to indicate the event was handled
//        });


        // Initialize CardViews
        setupCardViews();
    }


    private void setupCardViews() {
        CardView cardBloodGlucose = findViewById(R.id.card_blood_glucose);
        cardBloodGlucose.setOnClickListener(view -> {
            String type = "BLOODGLUCOSE";
            Toast.makeText(MeasurementActivity.this, "Blood Glucose", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, Measurement_BloodGlucose_Activity.class);
                        Intent intent = new Intent(this, Measurement_BloodGlucoseList_Activity.class);
            intent.putExtra("measurement_type", type);
            startActivity(intent);
        });

        CardView cardBloodPressure = findViewById(R.id.card_blood_pressure);
        cardBloodPressure.setOnClickListener(view -> {
            String type = "BLOODPRESSURE";
            Toast.makeText(MeasurementActivity.this, "Blood Pressure", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Measurement_BloodPressureList_Activity.class);
            intent.putExtra("measurement_type", type);
            startActivity(intent);
        });

        CardView cardECG = findViewById(R.id.card_ecg);
        cardECG.setOnClickListener(view -> {
            String type = "ECG";
            Toast.makeText(MeasurementActivity.this, "ECG", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Measurement_ECG_Activity.class);
            intent.putExtra("measurement_type", type);
            startActivity(intent);
        });

        CardView cardTemperature = findViewById(R.id.card_temperature);
        cardTemperature.setOnClickListener(view -> {
            String type = "TEMPERATURE";
            Toast.makeText(MeasurementActivity.this, "Temperature", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Measurement_TempList_Activity.class);
            intent.putExtra("measurement_type", type);
            startActivity(intent);
        });

        CardView cardO2 = findViewById(R.id.card_o2);
        cardO2.setOnClickListener(view -> {
            String type = "SPO2";
            Toast.makeText(MeasurementActivity.this, "O2", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Measurement_O2List_Activity.class);
            intent.putExtra("measurement_type", type);
            startActivity(intent);
        });

        CardView cardRespiratoryRate = findViewById(R.id.card_respiratory_rate);
        cardRespiratoryRate.setOnClickListener(view -> {
            String type = "RESPIRATORYRATE";
            Toast.makeText(MeasurementActivity.this, "Respiratory Rate", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Measurement_RespiratoryRateList_Activity.class);
            intent.putExtra("measurement_type", type);
            startActivity(intent);
        });
    }
    private void logout() {
        // Clear the shared preferences (logout user)
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(MeasurementActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
