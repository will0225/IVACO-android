package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;  // Import Button
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

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;
    private Button buttonCreateAppointment; // Declare button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        // Set window insets to handle padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find buttons
        ImageButton buttonBack = findViewById(R.id.buttonBack);

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Drawer and navigation setup
        main = findViewById(R.id.main);
        buttonDrawer = findViewById(R.id.buttonDrawer);
        navigationView = findViewById(R.id.navigationView);

        // Open the drawer on button click
        buttonDrawer.setOnClickListener(view -> main.open());

        // Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navProfile) {
                Toast.makeText(ReportActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navReports) {
                Toast.makeText(ReportActivity.this, "Reports Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportActivity.this, ReportActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navTeleMed) {
                Toast.makeText(ReportActivity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportActivity.this, TeleMedActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navLogout) {
                // Handle logout logic here
                Toast.makeText(ReportActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
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
        TableLayout tableLayout = findViewById(R.id.report_table);

        // Remove the placeholder row if it exists
        TableRow placeholderRow = findViewById(R.id.placeholder_report_row);
        if (placeholderRow != null) {
            tableLayout.removeView(placeholderRow);
        }

        // Sample data from your database (replace with actual data)
        List<ReportActivity.Report> reportList = getReportFromDatabase();

        if (reportList.isEmpty()) {
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
            for (ReportActivity.Report report : reportList) {
                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.WHITE); // Set white background for the row

                //  Name
                TextView NameTextView = new TextView(this);
                NameTextView.setText(report.getName());
                NameTextView.setPadding(8, 8, 8, 8);
                NameTextView.setGravity(Gravity.CENTER);
                NameTextView.setTextColor(Color.BLACK);
                NameTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(NameTextView);

                // EMR
                TextView EMRTextView = new TextView(this);
                EMRTextView.setText(report.getEMR()); // Updated method name
                EMRTextView.setPadding(8, 8, 8, 8);
                EMRTextView.setGravity(Gravity.CENTER);
                EMRTextView.setTextColor(Color.BLACK);
                EMRTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(EMRTextView);

                // Report
                TextView ReportTextView = new TextView(this);
                ReportTextView.setText(report.getReport());
                ReportTextView.setPadding(8, 8, 8, 8);
                ReportTextView.setGravity(Gravity.CENTER);
                ReportTextView.setTextColor(Color.BLACK);
                ReportTextView.setBackgroundColor(Color.WHITE);
                tableRow.addView(ReportTextView);

                // Department
                TextView DepartmentTextView = new TextView(this);
                DepartmentTextView.setText(report.getDepartment());
                DepartmentTextView.setPadding(8, 8, 8, 8);
                DepartmentTextView.setGravity(Gravity.CENTER);
                DepartmentTextView.setTextColor(Color.BLACK);
                DepartmentTextView.setBackgroundColor(Color.WHITE);
                tableRow.addView(DepartmentTextView);

                // Doctor
                TextView DoctorTextView = new TextView(this);
                DoctorTextView.setText(report.getDoctor());
                DoctorTextView.setPadding(8, 8, 8, 8);
                DoctorTextView.setGravity(Gravity.CENTER);
                DoctorTextView.setTextColor(Color.BLACK);
                DoctorTextView.setBackgroundColor(Color.WHITE);
                tableRow.addView(DoctorTextView);

                // Add the row to the table
                tableLayout.addView(tableRow);
            }
        }
    }

    private List<ReportActivity.Report> getReportFromDatabase() {
        // Placeholder method, replace with actual database call
        return List.of(
                new ReportActivity.Report("John Doe", "123",  "Complete", " A", "Dr. Smith"),
                new ReportActivity.Report("Jane Smith", "456",  "Pending", " B", "Dr. Johnson"),
                new ReportActivity.Report("Alex Johnson", "789",  "Complete", " C", "Dr. Davis"),
                new ReportActivity.Report("Emily Davis", "101",  "Cancelled", " D", "Dr.Lee")
        );
    }

    // Report model class

    public static class Report {
        private final String name;
        private final String emr;
        private final String report;
        private final String department;
        private final String doctor;

        public Report(String name, String emr, String report, String department, String doctor) {
            this.name = name;
            this.emr = emr;
            this.report = report;
            this.department = department;
            this.doctor = doctor;
        }

        public String getName() {
            return name;
        }
        public String getEMR() {
            return emr; // Updated method name for consistency
        }

        public String getReport() {
            return report;
        }

        public String getDepartment() {
            return department;
        }
        public String getDoctor() {
            return doctor;
        }
    }

    // Logout method to clear preferences and redirect to login
    private void logout() {
        // Clear the shared preferences (logout user)
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(ReportActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
