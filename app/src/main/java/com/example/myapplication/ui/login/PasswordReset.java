package com.example.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.LoginScreen;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.ForgotPasswordRequest;
import com.example.myapplication.models.ForgotPasswordResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordReset extends AppCompatActivity {

    private TextView privacyPolicyTextView, termsConditionsTextView;
    private EditText etUsername;
    private MaterialButton btnSubmit, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        privacyPolicyTextView = findViewById(R.id.tv_privacy_policy);
        termsConditionsTextView = findViewById(R.id.tv_terms_conditions);

        // Set OnClickListeners for text views and buttons
        setOnClickListeners();

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Set OnClickListeners for buttons and text views
    private void setOnClickListeners() {
        btnSubmit.setOnClickListener(v -> submitForm());
        btnCancel.setOnClickListener(v -> navigateBackToLogin());
        privacyPolicyTextView.setOnClickListener(v -> startActivity(new Intent(this, PrivacyPolicy.class)));
        termsConditionsTextView.setOnClickListener(v -> startActivity(new Intent(this, TermsConditions.class)));
    }

    // Handle form submission and password reset request
    private void submitForm() {
        String email = etUsername.getText().toString().trim();

        // Validate the email input
        if (!isValidEmail(email)) {
            etUsername.setError("Please enter a valid email address");
            return;
        }

        // Make the API call to send the forgot password request
        sendForgotPasswordRequest(email);
    }

    // Check if email is valid
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Send forgot password request using Retrofit
    private void sendForgotPasswordRequest(String email) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        apiService.forgotPassword(request).enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(PasswordReset.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PasswordReset.this, "Error: Could not process request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Toast.makeText(PasswordReset.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Navigate back to the login screen
    private void navigateBackToLogin() {
        etUsername.setText(""); // Clear the email field
        Intent intent = new Intent(PasswordReset.this, LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
