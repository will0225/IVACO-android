package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.UserProfile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private WebView mapWebView;
    private FusedLocationProviderClient fusedLocationClient;
    private Switch fingerprintToggle;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private EditText dobEditText;
    private Calendar calendar;
    private TextView welcomeMessage, firstNameTextView, lastNameTextView, phoneTextView, emailTextView,socialSecurityNumber,subscriber,weightEditText,heightEditText,languageEditText,measurement_systemEditText,timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Get the token from the intent
        String token = getIntent().getStringExtra("TOKEN");
        if (token != null) {
            saveUserToken(token); // Save the token after successful login
        } else {
            Log.e("ProfileActivity", "Token is null"); // Log if token is null
        }

        // Check if the token is already saved in SharedPreferences
        String savedToken = getUserToken(); // Retrieve token from SharedPreferences
        if (savedToken != null) {
            Log.d("ProfileActivity", "Token found: " + savedToken);
            // Fetch user profile data
            fetchUserProfile(savedToken); // Pass the retrieved token
        } else {
            Log.e("ProfileActivity", "No token found in SharedPreferences");
            Toast.makeText(this, "No token found. Please log in again.", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        mapWebView = findViewById(R.id.map_webview);
        webViewSetup();
        welcomeMessage = findViewById(R.id.welcome_message);
        socialSecurityNumber = findViewById(R.id.ssn);
        subscriber = findViewById(R.id.subscriber_id);
        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        phoneTextView = findViewById(R.id.phone);
        emailTextView = findViewById(R.id.email);
        dobEditText = findViewById(R.id.dob);
        weightEditText = findViewById(R.id.weight);
        heightEditText = findViewById(R.id.height);
        languageEditText = findViewById(R.id.language);
        measurement_systemEditText = findViewById(R.id.measurement_system);
        timeEditText = findViewById(R.id.time);

        // Date Picker setup
        calendar = Calendar.getInstance();
        dobEditText.setOnClickListener(v -> showDatePicker());

        // Set up fingerprint toggle
        fingerprintSetup();

        // Inside onCreate method
        Button closeButton = findViewById(R.id.btn_close);
        closeButton.setOnClickListener(v -> onBackPressed());

    }

    // Method to save the token securely
    private void saveUserToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();  // Don't forget to apply the changes
        Log.d("ProfileActivity", "Token saved: " + token); // Log the saved token
    }

    // Method to retrieve the user's token
    private String getUserToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("token", null); // returns null if token is not found
    }

    private void webViewSetup() {
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mapWebView.setWebViewClient(new WebViewClient());
        String karachiMapUrl = "https://www.openstreetmap.org/export/embed.html?bbox=66.885%2C24.850%2C67.155%2C25.150&layer=mapnik";
        mapWebView.loadUrl(karachiMapUrl);
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    dobEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void fingerprintSetup() {
        fingerprintToggle = findViewById(R.id.fingerprint_toggle);
        executor = Executors.newSingleThreadExecutor();
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Fingerprint registered successfully", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onAuthenticationFailed() {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show());
            }
        });

        fingerprintToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Fingerprint Authentication")
                        .setDescription("Log in using your fingerprint")
                        .setNegativeButtonText("Cancel")
                        .build();
                biometricPrompt.authenticate(promptInfo);
            } else {
                Toast.makeText(ProfileActivity.this, "Fingerprint disabled", Toast.LENGTH_SHORT).show();
            }
        });
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
                    Log.d("ProfileActivity", "UserProfile: " + userProfile.toString()); // Add this line
                    populateUserProfile(userProfile);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUserProfile(UserProfile userProfile) {

        if (welcomeMessage != null) {
            String welcomeText = "Welcome " + userProfile.getFirstName();
            Log.d("ProfileActivity", welcomeText);
            welcomeMessage.setText(welcomeText);
        } else {
            Log.e("ProfileActivity", "welcomeMessage is null");
        }
        if (socialSecurityNumber != null) {
            String socialsecuritynumber =  userProfile.getSocialSecurityNumber();
            Log.d("ProfileActivity", "SSN: " + socialsecuritynumber);
            ((EditText) socialSecurityNumber).setText(socialsecuritynumber);
        } else {
            Log.e("ProfileActivity", "socialSecurityNumber is null");
        }
        if (subscriber != null) {
            String sub = userProfile.getsubscriber();
            Log.d("ProfileActivity", "subscriber: " + sub);
            ((EditText) subscriber).setText(sub);
        } else {
            Log.e("ProfileActivity", "subscriber is null");
        }
        if (firstNameTextView != null) {
            String firstName = userProfile.getFirstName();
            Log.d("ProfileActivity", "First Name: " + firstName);
            ((EditText) firstNameTextView).setText(firstName);
        } else {
            Log.e("ProfileActivity", "firstNameTextView is null");
        }
        if (lastNameTextView != null) {
            String lastName = userProfile.getLastName();
            Log.d("ProfileActivity", "Last Name: " + lastName);
            ((EditText) lastNameTextView).setText(lastName);
        } else {
            Log.e("ProfileActivity", "lastNameTextView is null");
        }
        if (phoneTextView != null) {
            String phoneNumber = userProfile.getPhoneNumber();
            Log.d("ProfileActivity", "Phone Number: " + phoneNumber);
            ((EditText) phoneTextView).setText(phoneNumber);
        } else {
            Log.e("ProfileActivity", "phoneTextView is null");
        }
        if (emailTextView != null) {
            String email = userProfile.getEmail();
            Log.d("ProfileActivity", "Email: " + email);
            ((EditText) emailTextView).setText(email);
        } else {
            Log.e("ProfileActivity", "emailTextView is null");
        }
        if (dobEditText != null) {
            String dob = userProfile.getDob().trim(); // Trim whitespace
            Log.d("ProfileActivity", "Raw Date of Birth: " + dob);

            try {
                // Parse the raw DOB string (assuming it's in "yyyy-MM-dd" format)
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = originalFormat.parse(dob);

                // Format the DOB to "MM/dd/yyyy"
                SimpleDateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                String formattedDob = targetFormat.format(date);

                // Set the formatted date in the EditText
                dobEditText.setText(formattedDob);
                Log.d("ProfileActivity", "Formatted Date of Birth: " + formattedDob);
            } catch (ParseException e) {
                Log.e("ProfileActivity", "Error parsing Date of Birth: " + e.getMessage());
                // Fallback to setting raw DOB if parsing fails
                dobEditText.setText(dob);
            }
        } else {
            Log.e("ProfileActivity", "dobEditText is null");
        }

        if (weightEditText != null) {
            String weight = userProfile.getWeight();
            Log.d("ProfileActivity", "Weight: " + weight);
            ((EditText) weightEditText).setText(weight);
        } else {
            Log.e("ProfileActivity", "weightEditText is null");
        }
        if (heightEditText != null) {
            String height = userProfile.getHeight();
            Log.d("ProfileActivity", "Height: " + height);
            ((EditText) heightEditText).setText(height);
        } else {
            Log.e("ProfileActivity", "heightEditText is null");
        }
        if (languageEditText != null) {
            String language = userProfile.getLanguage();
            Log.d("ProfileActivity", "Language: " + language);
            ((EditText) languageEditText).setText(language);
        } else {
            Log.e("ProfileActivity", "languageEditText is null");
        }
        if (measurement_systemEditText != null) {
            String measurement_system = userProfile.getMeasurement_System();
            Log.d("ProfileActivity", "Measurement_System: " + measurement_system);
            ((EditText) measurement_systemEditText).setText(measurement_system);
        } else {
            Log.e("ProfileActivity", "measurement_systemEditText is null");
        }
        if (timeEditText != null) {
            String time = userProfile.getTime();
            Log.d("ProfileActivity", "Time: " + time);
            ((EditText) timeEditText).setText(time);
        } else {
            Log.e("ProfileActivity", "timeEditText is null");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This will return to the previous screen
    }

}
