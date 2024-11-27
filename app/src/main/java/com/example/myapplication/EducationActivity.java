package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.EducationAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.Education;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;  // Import this from retrofit2
import retrofit2.Response;

public class EducationActivity extends AppCompatActivity {

    private DrawerLayout main;
    private ImageButton buttonDrawer;
    private NavigationView navigationView;
    private String authToken;
    private RecyclerView recyclerView;
    private EducationAdapter educationAdapter;
    private List<Education> educationList; // Store the fetched data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education);

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
                Toast.makeText(EducationActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EducationActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navReports) {
                Toast.makeText(EducationActivity.this, "Reports Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EducationActivity.this, ReportActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navTeleMed) {
                Toast.makeText(EducationActivity.this, "TeleMedicine Menu Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EducationActivity.this, TeleMedActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.navLogout) {
                // Handle logout logic here
                Toast.makeText(EducationActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
                logout();
            }
            // Close the drawer after an item is clicked
            main.closeDrawer(GravityCompat.START);
            return true; // Return true to indicate the event was handled
        });

        // Retrieve user ID and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("token", null);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEducation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch education data from the API
        fetchEducationData();
    }


    // Fetch education data from the API
    private void fetchEducationData() {
        // Get an instance of the API service
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Call the API method to fetch education data, passing the auth token in the header
        Call<List<Education>> call = apiService.getEducationData("Bearer " + authToken);

        // Enqueue the call to execute asynchronously
        call.enqueue(new retrofit2.Callback<List<Education>>() {
            @Override
            public void onResponse(Call<List<Education>> call, Response<List<Education>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Education> educationList = response.body();
                    educationAdapter = new EducationAdapter(EducationActivity.this, educationList); // Create adapter
                    recyclerView.setAdapter(educationAdapter); // Set adapter to RecyclerView

                    // Log the fetched data
                    for (Education education : educationList) {
                        Log.d("EducationActivity", "Title: " + education.getTitle());
                        Log.d("EducationActivity", "Content: " + education.getContent());
                        Log.d("EducationActivity", "Author: " + education.getAuthor().getFirstName() + " " + education.getAuthor().getLastName());
                        Log.d("EducationActivity", "Likes Count: " + education.getLikesCount());
                        Log.d("EducationActivity", "URI: " + education.getUri());
                        Log.d("EducationActivity", "Views Count: " + education.getViewsCount());

                        // Check if createdAt is not null before logging it
                        if (education.getCreatedAt() != null) {
                            Log.d("EducationActivity", "Created At: " + education.getCreatedAt().toString());
                        } else {
                            Log.d("EducationActivity", "Created At: null");
                        }
                    }
                } else {
                    Log.e("EducationActivity", "Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Education>> call, Throwable t) {
                Log.e("EducationActivity", "API Call Failed: " + t.getMessage());
            }
        });
    }


    // Logout method to clear preferences and redirect to login
    private void logout() {
        // Clear the shared preferences (logout user)
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(EducationActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
