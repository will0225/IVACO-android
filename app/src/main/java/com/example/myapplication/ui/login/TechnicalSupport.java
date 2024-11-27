package com.example.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.LoginScreen;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

public class TechnicalSupport extends AppCompatActivity {

    private TextView privacyPolicyTextView, termsConditionsTextView;
    private EditText etInput;
    private MaterialButton btnSubmit, btnCancel;
    private CheckBox cbForgotPassword, cbSignErr, cbFindingErr, cbShowingErr, cbOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_technical_support);

        // Initialize views
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        privacyPolicyTextView = findViewById(R.id.tv_privacy_policy);
        termsConditionsTextView = findViewById(R.id.tv_terms_conditions);
        etInput = findViewById(R.id.et_input);

        // Initialize checkboxes
        cbForgotPassword = findViewById(R.id.cb_forgotPassword);
        cbSignErr = findViewById(R.id.cb_SignErr);
        cbFindingErr = findViewById(R.id.cb_findingErr);
        cbShowingErr = findViewById(R.id.cb_showingErr);
        cbOthers = findViewById(R.id.cb_others);

        // Set OnClickListeners for the text views
        setOnClickListeners();

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle Submit button click
        btnSubmit.setOnClickListener(v -> handleSubmit());

        // Handle Cancel button click
        btnCancel.setOnClickListener(v -> handleCancel());
    }

    // Set OnClickListeners for the text views
    private void setOnClickListeners() {
        privacyPolicyTextView.setOnClickListener(v -> navigateToPrivacyPolicy());
        termsConditionsTextView.setOnClickListener(v -> navigateToTermsConditions());
    }

    // Handle submit logic
    private void handleSubmit() {
        // Validate input
        String input = etInput.getText().toString();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "Please enter your input.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if at least one checkbox is selected
        if (!cbForgotPassword.isChecked() && !cbSignErr.isChecked() && !cbFindingErr.isChecked() &&
                !cbShowingErr.isChecked() && !cbOthers.isChecked()) {
            Toast.makeText(this, "Please select an issue.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Process the form submission (this is where you can add logic to send data to a server or database)
        Toast.makeText(this, "Form submitted successfully!", Toast.LENGTH_SHORT).show();
    }

    // Handle cancel logic
    private void handleCancel() {
        // Clear all input fields and checkboxes
        etInput.setText("");
        cbForgotPassword.setChecked(false);
        cbSignErr.setChecked(false);
        cbFindingErr.setChecked(false);
        cbShowingErr.setChecked(false);
        cbOthers.setChecked(false);

        // Navigate back to the LoginScreen
        Intent intent = new Intent(TechnicalSupport.this, LoginScreen.class);
        startActivity(intent);
        finish();  // Optionally call finish() to close the current activity
    }

    // Navigation methods for various other activities
    public void navigateToPrivacyPolicy() {
        startActivity(new Intent(this, PrivacyPolicy.class));
    }

    public void navigateToTermsConditions() {
        startActivity(new Intent(this, TermsConditions.class));
    }
}
