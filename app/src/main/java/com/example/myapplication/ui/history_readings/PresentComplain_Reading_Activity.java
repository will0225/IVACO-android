package com.example.myapplication.ui.history_readings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class PresentComplain_Reading_Activity extends AppCompatActivity {

    private EditText etDetails, etDate;
    private Button btnSubmit, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_complain_reading); // Update with your XML layout file name

        // Initialize views
        etDetails = findViewById(R.id.et_pc_details);
        etDate = findViewById(R.id.et_pc_date);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        // Submit Button Click Listener
        btnSubmit.setOnClickListener(v -> submitAppointment());

        // Cancel Button Click Listener
        btnCancel.setOnClickListener(v -> finish()); // Close the activity
    }

    private void submitAppointment() {
        String name = etDetails.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Process the appointment (e.g., save it to the database or send it to the server)
        // For now, just show a success message
        Toast.makeText(this, " Present Complaints Submitted successfully!", Toast.LENGTH_SHORT).show();

        // Optionally, clear the fields after submission
        clearFields();
    }

    private void clearFields() {
        etDetails.setText("");
        etDate.setText("");
    }
}
