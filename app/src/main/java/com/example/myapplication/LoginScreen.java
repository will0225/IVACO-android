package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.ui.login.PasswordReset;
import com.example.myapplication.ui.login.PrivacyPolicy;
import com.example.myapplication.ui.login.TechnicalSupport;
import com.example.myapplication.ui.login.TermsConditions;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private TextView forgetPasswordTextView, privacyPolicyTextView, termsConditionsTextView;
    private MaterialButton loginButton,btnCancel;
    private ImageView showPasswordImageView;
    private boolean isPasswordVisible = false;
    private TextView passwordGuidelinesTextView;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Initialize views
        initializeViews();

        // Setup biometric authentication
        setupBiometricPrompt();

        // Set click listener to toggle password visibility
        showPasswordImageView.setOnClickListener(v -> togglePasswordVisibility());

        // Handle login button click
        loginButton.setOnClickListener(v -> attemptFingerprintLogin());

        // Handle other text view clicks
        setOnClickListeners();

        // Load saved credentials if "Remember Me" was checked
        loadSavedCredentials();
    }

    // Initialize views method
    private void initializeViews() {
        usernameEditText = findViewById(R.id.et_username);
        passwordEditText = findViewById(R.id.et_password);
        rememberMeCheckBox = findViewById(R.id.cb_remember_me);
        forgetPasswordTextView = findViewById(R.id.tv_forget_password);
//        technicalSupportTextView = findViewById(R.id.tv_technical_support);
        privacyPolicyTextView = findViewById(R.id.tv_privacy_policy);
        termsConditionsTextView = findViewById(R.id.tv_terms_conditions);
        loginButton = findViewById(R.id.btn_login);
//        btnCancel = findViewById(R.id.btn_cancel);
        showPasswordImageView = findViewById(R.id.iv_show_password);


        // Set up cancel button to show exit confirmation dialog
//        btnCancel.setOnClickListener(v -> {
//            new android.app.AlertDialog.Builder(LoginScreen.this)
//                    .setTitle("Exit Confirmation")
//                    .setMessage("Are you sure you want to exit?")
//                    .setPositiveButton("Yes", (dialog, which) -> finish()) // Close the app if the user presses "Yes"
//                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Dismiss the dialog if the user presses "No"
//                    .show();
//        });
    }

    // Setup biometric authentication
    private void setupBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginScreen.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginScreen.this, "Fingerprint recognized", Toast.LENGTH_SHORT).show();
                navigateToDashboard();  // Navigate to the dashboard on success
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginScreen.this, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginScreen.this, "Error: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the prompt info
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Login")
                .setDescription("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();
    }

    // Attempt fingerprint login if fingerprint is enabled
    private void attemptFingerprintLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean fingerprintEnabled = sharedPreferences.getBoolean("fingerprintEnabled", false);

        if (fingerprintEnabled) {
            BiometricManager biometricManager = BiometricManager.from(this);
            if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo);
            } else {
                Toast.makeText(this, "Biometric authentication not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            attemptLogin(); // Proceed with normal login if fingerprint is not enabled
        }
    }

    // Save user details to SharedPreferences
    private void saveUserDetails(LoginResponse loginResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save all relevant fields from LoginResponse
        editor.putString("id", loginResponse.getId());
        editor.putString("first_name", loginResponse.getFirst_name());
        editor.putString("middle_name", loginResponse.getMiddle_name());
        editor.putString("last_name", loginResponse.getLast_name());
        editor.putString("email", loginResponse.getEmail());
        editor.putString("phone_number", loginResponse.getPhone_number());
        editor.putString("username", loginResponse.getUsername());
        editor.putString("role", loginResponse.getRole());
        editor.putString("uri", loginResponse.getUri());
        editor.putBoolean("active", loginResponse.isActive());
        editor.putString("organization_id", loginResponse.getOrganization_id());
        editor.putBoolean("isPincode", loginResponse.isPincode());
        editor.putString("token", loginResponse.getToken()); // Also save the token here
        editor.apply(); // Apply changes
    }
    // Attempt to log in with username and password
    private void attemptLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInput(username, password)) return;

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Use RetrofitClient to get the Retrofit instance
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Make the API call
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss(); // Dismiss dialog here
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken(); // Adjust based on your response model
//                    setToken(loginResponse.getToken()); // Save the token here
                    saveUserDetails(loginResponse); // Save all user details
                    if (rememberMeCheckBox.isChecked()) {
                        saveLoginCredentials(username, password);
                    }
                    Toast.makeText(LoginScreen.this, "Login successful!", Toast.LENGTH_SHORT).show(); // Success message
                    Log.d("LoginSuccess", "Token: " + loginResponse.getToken());
                    Log.d("LoginSuccess", "id: " + loginResponse.getId());

                    navigateToDashboard(); // Proceed to the dashboard
                } else {
                    Log.e("LoginError", "Error: " + response.code() + " - " + response.message());
                    Toast.makeText(LoginScreen.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss(); // Dismiss dialog here
                Toast.makeText(LoginScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate user input
    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }

        return true;
    }

    // Save login credentials
    private void saveLoginCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    // Load saved credentials
    private void loadSavedCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        usernameEditText.setText(savedUsername);
        passwordEditText.setText(savedPassword);
        rememberMeCheckBox.setChecked(!savedUsername.isEmpty()); // Set checkbox based on saved username
    }

    // Navigate to the main dashboard
    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the login activity
    }

    // Set OnClickListeners for the text views
    private void setOnClickListeners() {
        forgetPasswordTextView.setOnClickListener(v -> navigateToPasswordReset());
        privacyPolicyTextView.setOnClickListener(v -> navigateToPrivacyPolicy());
        termsConditionsTextView.setOnClickListener(v -> navigateToTermsConditions());
//        technicalSupportTextView.setOnClickListener(v -> navigateToTechnicalSupport());
    }
    private void showPasswordGuidelinesDialog() {
        new android.app.AlertDialog.Builder(LoginScreen.this)
                .setTitle("Password Guidelines")
                .setMessage("Your password should:\n- Be at least 8 characters long\n- Include both uppercase and lowercase letters\n- Contain at least one number\n- Include at least one special character (e.g., @, #, $)")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }


    // Navigation methods for various other activities
    public void navigateToPrivacyPolicy() {startActivity(new Intent(this, PrivacyPolicy.class));}
    public void navigateToPasswordReset() {startActivity(new Intent(this, PasswordReset.class));}
    public void navigateToTechnicalSupport() {startActivity(new Intent(this, TechnicalSupport.class));}
    public void navigateToTermsConditions() {startActivity(new Intent(this, TermsConditions.class));}
    // Toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPasswordImageView.setImageResource(R.drawable.baseline_remove_red_eye_24);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPasswordImageView.setImageResource(R.drawable.baseline_visibility_off_24);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

}
