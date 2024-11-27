package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MedicationActivity extends AppCompatActivity {

    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;
    private Button buttonCreateAppointment; // Declare button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medication);

        // Set window insets to handle padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Drawer and navigation setup
        main = findViewById(R.id.main);
        buttonDrawer = findViewById(R.id.buttonDrawer);
        navigationView = findViewById(R.id.navigationView);

        // Open the drawer on button click
        buttonDrawer.setOnClickListener(view -> main.open());

        // Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navProfile) {
                Toast.makeText(MedicationActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MedicationActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navReports) {
                Toast.makeText(MedicationActivity.this, "Reports Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MedicationActivity.this, ReportActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navTeleMed) {
                Toast.makeText(MedicationActivity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MedicationActivity.this, TeleMedActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navLogout) {
                // Handle logout logic here
                Toast.makeText(MedicationActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
                logout();
            }
            // Close the drawer after an item is clicked
            main.closeDrawer(GravityCompat.START);
            return true; // Return true to indicate the event was handled
        });

        // Populate the history table when the activity is created
        populateReportTable(); // Add this call to populate the table
    }

    private void populateReportTable() {
        TableLayout tableLayout = findViewById(R.id.medication_table);

        // Remove the placeholder row if it exists
        TableRow placeholderRow = findViewById(R.id.placeholder_medication_row);
        if (placeholderRow != null) {
            tableLayout.removeView(placeholderRow);
        }

        // Sample data from your database (replace with actual data)
        List<Medication> medicationList = fetchSampleMedications();

        if (medicationList.isEmpty()) {
            // If no data is available, add a placeholder row programmatically
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE); // White background for the row

            TextView placeholderTextView = new TextView(this);
            placeholderTextView.setText("No data available");
            placeholderTextView.setPadding(8, 8, 8, 8);
            placeholderTextView.setGravity(Gravity.CENTER);
            placeholderTextView.setTextColor(Color.BLACK);
            placeholderTextView.setBackgroundColor(Color.WHITE); // White background for the text

            tableRow.addView(placeholderTextView);

            // Add the placeholder row to the table
            tableLayout.addView(tableRow);
        } else {
            // Add the data rows if history is available
            for (Medication medication : medicationList) {
                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.WHITE); // Set white background for the row

                // Name
                TextView nameTextView = new TextView(this);
                nameTextView.setText(medication.getName());
                nameTextView.setPadding(8, 8, 8, 8);
                nameTextView.setGravity(Gravity.CENTER);
                nameTextView.setTextColor(Color.BLACK);
                nameTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(nameTextView);

                // Dosage
                TextView dosageTextView = new TextView(this);
                dosageTextView.setText(medication.getDosage());
                dosageTextView.setPadding(8, 8, 8, 8);
                dosageTextView.setGravity(Gravity.CENTER);
                dosageTextView.setTextColor(Color.BLACK);
                dosageTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(dosageTextView);

                // Frequency
                TextView frequencyTextView = new TextView(this);
                frequencyTextView.setText(medication.getFrequency());
                frequencyTextView.setPadding(8, 8, 8, 8);
                frequencyTextView.setGravity(Gravity.CENTER);
                frequencyTextView.setTextColor(Color.BLACK);
                frequencyTextView.setBackgroundColor(Color.WHITE);
                tableRow.addView(frequencyTextView);

                // Start Date
                TextView startDateTextView = new TextView(this);
                startDateTextView.setText(medication.getStartDate());
                startDateTextView.setPadding(8, 8, 8, 8);
                startDateTextView.setGravity(Gravity.CENTER);
                startDateTextView.setTextColor(Color.BLACK);
                startDateTextView.setBackgroundColor(Color.WHITE);
                tableRow.addView(startDateTextView);

                // End Date
                TextView endDateTextView = new TextView(this);
                endDateTextView.setText(medication.getEndDate());
                endDateTextView.setPadding(8, 8, 8, 8);
                endDateTextView.setGravity(Gravity.CENTER);
                endDateTextView.setTextColor(Color.BLACK);
                endDateTextView.setBackgroundColor(Color.WHITE);
                tableRow.addView(endDateTextView);

                // Add the row to the table
                tableLayout.addView(tableRow);
            }
        }
    }

    private List<Medication> fetchSampleMedications() {
        // Placeholder method, replace with actual database call
        List<Medication> medicationList = new ArrayList<>();

        // Creating sample medications with dates using Calendar
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        medicationList.add(new Medication("John Doe", "123", "Complete", sdf.format(calendar.getTime()), sdf.format(calendar.getTime())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);  // Incrementing date for next medication
        medicationList.add(new Medication("Jane Smith", "456", "Pending", sdf.format(calendar.getTime()), sdf.format(calendar.getTime())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        medicationList.add(new Medication("Alex Johnson", "789", "Complete", sdf.format(calendar.getTime()), sdf.format(calendar.getTime())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        medicationList.add(new Medication("Emily Davis", "101", "Cancelled", sdf.format(calendar.getTime()), sdf.format(calendar.getTime())));

        return medicationList;
    }

    // Report model class
    public static class Medication {
        private final String name;
        private final String dosage;
        private final String frequency;
        private final String startdate;
        private final String enddate;

        public Medication(String name, String dosage, String frequency, String startdate, String enddate) {
            this.name = name;
            this.dosage = dosage;
            this.frequency = frequency;
            this.startdate = startdate;
            this.enddate = enddate;
        }

        public String getName() {
            return name;
        }

        public String getDosage() {
            return dosage;
        }

        public String getFrequency() {
            return frequency;
        }

        public String getStartDate() {
            return startdate;
        }

        public String getEndDate() {
            return enddate;
        }
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply(); // Apply changes asynchronously

        Intent intent = new Intent(MedicationActivity.this, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close current activity
    }
}
