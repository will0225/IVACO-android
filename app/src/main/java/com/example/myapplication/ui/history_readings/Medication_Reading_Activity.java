package com.example.myapplication.ui.history_readings;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.Calendar;

public class Medication_Reading_Activity extends AppCompatActivity {

    private EditText etDetails, etDate;
    private Button btnSubmit, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_reading); // Update with your XML layout file name

        // Initialize views
        etDetails = findViewById(R.id.et_med_details);
        etDate = findViewById(R.id.et_med_date);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        // Date Picker for Date field
        etDate.setOnClickListener(v -> showDatePicker());

        // Submit Button Click Listener
        btnSubmit.setOnClickListener(v -> submitAppointment());

        // Cancel Button Click Listener
        btnCancel.setOnClickListener(v -> finish()); // Close the activity
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDate.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
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
        Toast.makeText(this, "Medication Submitted successfully!", Toast.LENGTH_SHORT).show();

        // Optionally, clear the fields after submission
        clearFields();
    }

    private void clearFields() {
        etDetails.setText("");
        etDate.setText("");
    }
}
