package com.example.myapplication.ui.measurement_reading;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class RespiratoryRate_Reading_Activity extends AppCompatActivity {

    private EditText etRR;
    private Button btnCancel, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_respiratory_rate_reading);

        // Initialize UI components
        etRR = findViewById(R.id.et_rr);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSubmit = findViewById(R.id.btn_submit);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the Cancel button action
        btnCancel.setOnClickListener(v -> {
            // Navigate back to the measurements screen
            finish(); // This will close the current activity
        });

        // Set up the Submit button action
        btnSubmit.setOnClickListener(v -> {
            String RR = etRR.getText().toString(); // Use 'temp' in lowercase

            // Check if the field is empty
            if (RR.isEmpty()) {
                Toast.makeText(this, "Please enter your Respiratory Rate", Toast.LENGTH_SHORT).show(); // Correct message
            } else {
                // Show a message indicating the response has been submitted
                Toast.makeText(this, "Submitted: Temperature: " + RR, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
