package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.ui.history_readings.FamilyHistory_Reading_Activity;
import com.example.myapplication.ui.history_readings.Medication_Reading_Activity;
import com.example.myapplication.ui.history_readings.PresentComplain_Reading_Activity;
import com.example.myapplication.ui.history_readings.SurgicalProcedure_Reading_Activity;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class History_Activity extends AppCompatActivity {

    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
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
        buttonDrawer.setOnClickListener(view -> main.open());
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navProfile) {
                Toast.makeText(History_Activity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(History_Activity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
            else if (item.getItemId() == R.id.navReports) {
                Toast.makeText(History_Activity.this, "Reports Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(History_Activity.this, ReportActivity.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.navTeleMed) {
                Toast.makeText(History_Activity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(History_Activity.this, TeleMedActivity.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.navLogout) {
                // Handle logout logic here
                Toast.makeText(History_Activity.this, "Logging Out", Toast.LENGTH_SHORT).show();
                logout();
            }
            // Close the drawer after an item is clicked
            main.closeDrawer(GravityCompat.START);
            return true; // Return true to indicate the event was handled
        });

        // Initialize CardViews
        setupCardViews();

        // Populate the history table
        populateHistoryTable();
    }

    private void setupCardViews() {
        CardView cardPresentComplain = findViewById(R.id.btn_present_complain);
        cardPresentComplain.setOnClickListener(view -> {
            Toast.makeText(History_Activity.this, "Present Complain", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PresentComplain_Reading_Activity.class);
            startActivity(intent);
        });

        CardView cardFamilyHistory = findViewById(R.id.btn_family_history);
        cardFamilyHistory.setOnClickListener(view -> {
            Toast.makeText(History_Activity.this, "Family History", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FamilyHistory_Reading_Activity.class);
            startActivity(intent);
        });

        CardView cardSurgicalProcedure = findViewById(R.id.btn_surgical_procedure);
        cardSurgicalProcedure.setOnClickListener(view -> {
            Toast.makeText(History_Activity.this, "Surgical Procedures", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(this, SurgicalProcedure_Reading_Activity.class);
             startActivity(intent);
        });

        CardView cardMedication = findViewById(R.id.btn_medication);
        cardMedication.setOnClickListener(view -> {
            Toast.makeText(History_Activity.this, "Medication", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(this, Medication_Reading_Activity.class);
             startActivity(intent);
        });
    }

    private void populateHistoryTable() {
        TableLayout tableLayout = findViewById(R.id.history_table);

        // Remove the placeholder row if it exists
        TableRow placeholderRow = findViewById(R.id.placeholder_row);
        if (placeholderRow != null) {
            tableLayout.removeView(placeholderRow);
        }

        // Sample data from your database (replace with actual data)
        List<History> historyList = getHistoryFromDatabase();

        if (historyList.isEmpty()) {
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
            for (History history : historyList) {
                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.WHITE); // Set white background for the row

                TextView nameTextView = new TextView(this);
                nameTextView.setText(history.getName());
                nameTextView.setPadding(8, 8, 8, 8);
                nameTextView.setGravity(Gravity.CENTER);
                nameTextView.setTextColor(Color.BLACK);
                nameTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(nameTextView);

                TextView emrTextView = new TextView(this);
                emrTextView.setText(history.getEmr());
                emrTextView.setPadding(8, 8, 8, 8);
                emrTextView.setGravity(Gravity.CENTER);
                emrTextView.setTextColor(Color.BLACK);
                emrTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(emrTextView);

                TextView dateTextView = new TextView(this);
                dateTextView.setText(history.getDate());
                dateTextView.setPadding(8, 8, 8, 8);
                dateTextView.setGravity(Gravity.CENTER);
                dateTextView.setTextColor(Color.BLACK);
                dateTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(dateTextView);

                TextView statusTextView = new TextView(this);
                statusTextView.setText(history.getStatus());
                statusTextView.setPadding(8, 8, 8, 8);
                statusTextView.setGravity(Gravity.CENTER);
                statusTextView.setTextColor(Color.BLACK);
                statusTextView.setBackgroundColor(Color.WHITE); // White background for the text
                tableRow.addView(statusTextView);

                // Add the row to the table
                tableLayout.addView(tableRow);
            }
        }
    }

    private List<History> getHistoryFromDatabase() {
        // Placeholder method, replace with actual database call
        return List.of(
                new History("John Doe", "EMR123", "2023-09-25", "Complete"),
                new History("Jane Smith", "EMR124", "2023-09-26", "Pending"),
                new History("Alex Johnson", "EMR125", "2023-09-27", "Complete"),
                new History("Emily Davis", "EMR126", "2023-09-28", "Cancelled")
        );
    }

    private void logout() {
        // Clear the shared preferences (logout user)
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(History_Activity.this, LoginScreen.class);
        startActivity(intent);
        finish();  // Close the current activity
    }

    // History model class
    public static class History {
        private final String name;
        private final String emr;
        private final String date;
        private final String status;

        public History(String name, String emr, String date, String status) {
            this.name = name;
            this.emr = emr;
            this.date = date;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getEmr() {
            return emr;
        }

        public String getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }
    }
}
