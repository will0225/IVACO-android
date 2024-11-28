package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.UserProfile;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final long SWITCH_DELAY = 4000; // 4 seconds delay
    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;
    private String[] options = {"Temperature", "Blood Pressure", "SpO2", "Blood Glucose", "Respiratory Rate", "Weight"};
    private String[] statusColors = {"circle_red", "circle_green", "circle_yellow", "circle_blue"};
    private int currentIndex = 0;
    private TextView tvOption;
    private View statusCircle;
    private TextView tvWelcome;  // Welcome text view for showing username
    private Handler autoSwitchHandler;
    private Runnable autoSwitchRunnable;
    private TextView welcomeMessage, firstNameTextView, headerName, Role, Uri;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // Initialize views
//        tvOption = findViewById(R.id.tv_option);
//        statusCircle = findViewById(R.id.status_circle);
//        tvWelcome = findViewById(R.id.tv_welcome);  // Ensure this ID matches your layout
//        ImageView ivPrevious = findViewById(R.id.iv_previous);
//        ImageView ivNext = findViewById(R.id.iv_next);
//        welcomeMessage = findViewById(R.id.tv_welcome);
//        firstNameTextView = findViewById(R.id.first_name);
//        headerName = findViewById(R.id.user_name);

//        Uri = findViewById(R.id.profile_image);

        // Set initial option and status
//        updateOptionAndStatus();

        // Show the username in the welcome message
//        displayUsername();

        // Handle previous arrow click
//        ivPrevious.setOnClickListener(v -> {
//            stopAutoSwitch();
//            if (currentIndex > 0) {
//                currentIndex--;
//            } else {
//                currentIndex = options.length - 1; // Wrap around to the last option
//            }
//            updateOptionAndStatus();
//            startAutoSwitch();
//        });
//
//        // Handle next arrow click
//        ivNext.setOnClickListener(v -> {
//            stopAutoSwitch();
//            if (currentIndex < options.length - 1) {
//                currentIndex++;
//            } else {
//                currentIndex = 0; // Wrap around to the first option
//            }
//            updateOptionAndStatus();
//            startAutoSwitch();
//        });

        // Find buttons
//        ImageButton buttonBack = findViewById(R.id.buttonBack);

        // Set click listener for the back button
//        buttonBack.setOnClickListener(v -> onBackPressed());

        // Drawer and navigation setup
//        main = findViewById(R.id.main);
//        buttonDrawer = findViewById(R.id.buttonDrawer);
//        navigationView = findViewById(R.id.navigationView);
//        buttonDrawer.setOnClickListener(view -> main.openDrawer(GravityCompat.START));
//        navigationView.setNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.navProfile) {
//                Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            } else if (item.getItemId() == R.id.navReports) {
//                Toast.makeText(MainActivity.this, "Reports Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
//                startActivity(intent);
//            } else if (item.getItemId() == R.id.navTeleMed) {
//                Toast.makeText(MainActivity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, TeleMedActivity.class);
//                startActivity(intent);
//            } else if (item.getItemId() == R.id.navHistory) {
//                Toast.makeText(MainActivity.this, "History Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, History_Activity.class);
//                startActivity(intent);
//            } else if (item.getItemId() == R.id.navMedication) {
//                Toast.makeText(MainActivity.this, "Medication Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, MedicationActivity.class);
//                startActivity(intent);
//            } else if (item.getItemId() == R.id.navDevice) {
//                Toast.makeText(MainActivity.this, "Device Menu Clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, Device_Activity.class);startActivity(intent);
//            } else if (item.getItemId() == R.id.navLogout) {
//                // Handle logout logic here
//                Toast.makeText(MainActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
//                logout();
//            }
//            main.closeDrawer(GravityCompat.START);
//            return true;
//        });

        // Initialize buttons from GridLayout
        CardView btnMeasurement = findViewById(R.id.btn_measurement);
        CardView btnMessages = findViewById(R.id.btn_messages);
        CardView btnTeleMed = findViewById(R.id.btn_telemed);
        CardView btnEducation = findViewById(R.id.btn_education);
        CardView btnChat = findViewById(R.id.btn_chat);
        CardView btnNotifications = findViewById(R.id.btn_notifications);

        // Set click listeners for the buttons
        btnMeasurement.setOnClickListener(v -> navigateToMeasurement());
        btnEducation.setOnClickListener(v -> navigateToEducation());
        btnTeleMed.setOnClickListener(v -> navigateToTeleMedicine());
        btnMessages.setOnClickListener(v -> navigateToMessage());
        btnChat.setOnClickListener(v -> navigateToChat());

        btnNotifications.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Notifications clicked", Toast.LENGTH_SHORT).show();
        });

        // Initialize Handler and Runnable for automatic switching
        autoSwitchHandler = new Handler(Looper.getMainLooper());
        autoSwitchRunnable = new Runnable() {
            @Override
            public void run() {
                currentIndex = (currentIndex + 1) % options.length; // Loop around the options
//                updateOptionAndStatus();
                autoSwitchHandler.postDelayed(this, SWITCH_DELAY); // Schedule the next switch
            }
        };

        // Start automatic switching
        startAutoSwitch();
    }

    // Logout method to clear preferences and redirect to login
    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    // Display the username in the welcome message
    private void displayUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
//        tvWelcome.setText("Welcome, " + firstNameTextView + "!");
    }

    private void updateOptionAndStatus() {
//        tvOption.setText(options[currentIndex]);
//        int colorIndex = currentIndex % statusColors.length;
//        int statusResId = getResources().getIdentifier(statusColors[colorIndex], "drawable", getPackageName());
//        if (statusResId != 0) {
//            statusCircle.setBackgroundResource(statusResId);
//        }
    }

    private void startAutoSwitch() {
//        autoSwitchHandler.postDelayed(autoSwitchRunnable, SWITCH_DELAY);
    }

    private void stopAutoSwitch() {

//        autoSwitchHandler.removeCallbacks(autoSwitchRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoSwitch();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        startAutoSwitch();

        // Fetch user profile only if you have a valid token
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            fetchUserProfile(token);
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (main.isDrawerOpen(GravityCompat.START)) {
//            main.closeDrawer(GravityCompat.START);
//        } else {
//            new AlertDialog.Builder(this)
//                    .setTitle("Exit")
//                    .setMessage("Are you sure you want to exit?")
//                    .setPositiveButton("Yes", (dialog, which) -> finish()) // Close the activity
//                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
//                    .show();
//        }
//    }

    private void navigateToEducation() {Intent intent = new Intent(this, EducationActivity.class);
        startActivity(intent);
    }
    private void navigateToMeasurement() {Intent intent = new Intent(this, MeasurementActivity.class);
        startActivity(intent);
    }
    private void navigateToTeleMedicine() {Intent intent = new Intent(this, TeleMedActivity.class);
        startActivity(intent);
    }
    private void navigateToMessage() {Intent intent = new Intent(this,
//            Dashboard_2_Activity.class);
            TeleMedActivity.class
    );
        startActivity(intent);
    }
    private void navigateToChat() {Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
    private void fetchUserProfile(String token) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Add "Bearer " prefix to the token
        String bearerToken = "Bearer " + token;
        Call<UserProfile> call = apiService.getUserProfile(bearerToken);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile userProfile = response.body();
                    Log.d("MainActivity", "UserProfile: " + userProfile.toString());
                    populateUserProfile(userProfile);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void populateDrawer(UserProfile userProfile) {
        if (userProfile != null) {
            // Fetch the header view from the NavigationView
            View headerView = navigationView.getHeaderView(0);

            // Find the TextView for the user name in the header
            TextView userNameTextView = headerView.findViewById(R.id.user_name);

            // Set the user's first name
            String firstName = userProfile.getFirstName();
            if (userNameTextView != null) {
                userNameTextView.setText(firstName != null ? firstName : "Guest");
            }
        }
    }

    private void populateUserProfile(UserProfile userProfile) {
        if (userProfile != null) {
            String firstName = userProfile.getFirstName();

            // Set the welcome message
            if (welcomeMessage != null) {
                String welcomeText = "Welcome " + firstName ;
                Log.d("MainActivity", welcomeText);
//                welcomeMessage.setText(welcomeText);
            } else {
                Log.e("MainActivity", "welcomeMessage is null");
            }

            // Update the user name in the drawer header
//            populateDrawer(userProfile);
        } else {
            Log.e("MainActivity", "UserProfile is null");
        }
    }

}
