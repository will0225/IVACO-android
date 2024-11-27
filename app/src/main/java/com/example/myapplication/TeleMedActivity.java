package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.TeleMedAppointment;
import com.example.myapplication.adapter.AppointmentAdapter;
import com.example.myapplication.ui.telemed.AddAppointment_Activity;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeleMedActivity extends AppCompatActivity {

    private static final String TAG = "TeleMedActivity";
    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;
    private String token;

    // Declare your fields
    private TextView tvHeading, tvUsername, tvHospital;
    private Button btnCreateAppointment,btnView;
    private TextView tvRequestedAppointmentTitle, tvAppointmentTitle;
    private LinearLayout appointmentBoxes;
    private TextView appointmentDescription;
    private TextView appointmentAbout, appointmentType;
    private TextView startDate, endDate;
    private ImageView uri;
    private TextView firstName, role, status;
    private RecyclerView recyclerViewAppointments; // Moved here for better visibility

    private String appointmentId; // Variable to store appointment ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_med);
        Log.d(TAG, "Activity created");

        // Fetch token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        token = sharedPreferences.getString("token", ""); // Retrieve the token
        Log.d(TAG, "Retrieved token: " + token);

        // Check if the token is valid
        if (token.isEmpty()) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            // Redirect to login
            logout();
            return;
        }

        // Set window insets to handle padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Initialize your fields
        initializeViews();
        Log.d(TAG, "Views initialized");

        // Find buttons
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonDrawer = findViewById(R.id.buttonDrawer);
        navigationView = findViewById(R.id.navigationView);

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            onBackPressed();
        });

        // Open the drawer on button click
        buttonDrawer.setOnClickListener(view -> {
            Log.d(TAG, "Drawer opened");
            main.openDrawer(GravityCompat.START);
        });

        // Set OnClickListener for Create Appointment button
        btnCreateAppointment.setOnClickListener(view -> {
            Log.d(TAG, "Create Appointment button clicked");
            Intent intent = new Intent(TeleMedActivity.this, AddAppointment_Activity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView
        recyclerViewAppointments = findViewById(R.id.recycler_view_appointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        // Call API to fetch TeleMed data
        Log.d(TAG, "Fetching TeleMed appointments");
        fetchTeleMedAppointment();
    }

    private void initializeViews() {
        tvHeading = findViewById(R.id.tv_heading);
        tvUsername = findViewById(R.id.tv_username);
        tvHospital = findViewById(R.id.tv_hospital);
        btnCreateAppointment = findViewById(R.id.btn_create_appointment);
        tvRequestedAppointmentTitle = findViewById(R.id.tv_requested_appointment_title);
//        tvAppointmentTitle = findViewById(R.id.tv_appointment_title);
//        appointmentBoxes = findViewById(R.id.appointment_boxes);
        appointmentDescription = findViewById(R.id.appointment_description);
        appointmentAbout = findViewById(R.id.appointment_about);
        appointmentType = findViewById(R.id.appointment_type);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        uri = findViewById(R.id.uri);
        firstName = findViewById(R.id.first_name);
        role = findViewById(R.id.role);
        status = findViewById(R.id.status);
        btnView = findViewById(R.id.btn_view);
        Log.d(TAG, "Views initialized in initializeViews");
    }

    // Method to fetch TeleMed Appointment data
    private void fetchTeleMedAppointment() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<TeleMedAppointment>> call = apiService.getTeleMedAppointment("Bearer " + token);

        call.enqueue(new Callback<List<TeleMedAppointment>>() {
            @Override
            public void onResponse(Call<List<TeleMedAppointment>> call, Response<List<TeleMedAppointment>> response) {
                Log.d(TAG, "API Response received");
                if (response.isSuccessful()) {
                    List<TeleMedAppointment> appointments = response.body();
                    Log.d(TAG, "Response successful with " + (appointments != null ? appointments.size() : 0) + " appointments");
                    if (appointments != null && !appointments.isEmpty()) {
                        appointmentId = appointments.get(0).getId();
                        updateUI(appointments);
                    } else {
                        Toast.makeText(TeleMedActivity.this, "No appointments found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "No appointments found");
                    }
                } else {
                    Log.e(TAG, "API Error: " + response.code());
                    Toast.makeText(TeleMedActivity.this, "Error fetching appointments: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TeleMedAppointment>> call, Throwable t) {
                Log.e(TAG, "API Call Failed: ", t);
                Toast.makeText(TeleMedActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(List<TeleMedAppointment> appointments) {
        Log.d(TAG, "Updating UI with appointments list");
        AppointmentAdapter adapter = new AppointmentAdapter(this, appointments);
        recyclerViewAppointments.setAdapter(adapter);
    }

    // Logout method to clear preferences and redirect to login
    private void logout() {
        Log.d(TAG, "Logging out");
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(TeleMedActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
