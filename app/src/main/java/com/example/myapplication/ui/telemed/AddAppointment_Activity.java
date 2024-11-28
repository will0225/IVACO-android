package com.example.myapplication.ui.telemed;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TeleMedSearchAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.TeleMedSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAppointment_Activity extends AppCompatActivity {

    private Button btnSubmit, btnCancel;
    private EditText etAppointmentTitle, etAppointmentDescription;
    private Spinner spinnerAppointmentType, spinnerAppointmentAbout;
    private String authToken;
    private List<TeleMedSearch> teleMedSearchList;
    private TeleMedSearchAdapter adapter;
    private SearchView searchView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Retrieve user ID and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("token", null);

        // Log the token to debug
        if (authToken != null) {
            Log.d("AddAppointment", "Auth Token: " + authToken);
        } else {
            Log.e("AddAppointment", "Auth Token is null");
        }

        // Initialize views
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        etAppointmentTitle = findViewById(R.id.et_appointment_title);
        etAppointmentDescription = findViewById(R.id.et_appointment_description);
//        spinnerAppointmentType = findViewById(R.id.spinner_appointment_type);
//        spinnerAppointmentAbout = findViewById(R.id.spinner_appointment_about);
//        searchView = findViewById(R.id.searchView);
//        recyclerView = findViewById(R.id.resultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty list
        adapter = new TeleMedSearchAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Set up search functionality
        setupSearch();

//        TextView tvAppointmentDate = findViewById(R.id.tv_appointment_date);
//        tvAppointmentDate.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAppointment_Activity.this,
//                    (view, year1, month1, dayOfMonth) -> {
//                        tvAppointmentDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
//                    }, year, month, day);
//            datePickerDialog.show();
//        });

        btnSubmit.setOnClickListener(v -> handleSubmit());
        btnCancel.setOnClickListener(v -> handleCancel());

        // Initial fetch of users (empty query)
        if (authToken != null) {
            fetchTeleMedData("", authToken);
        } else {
            Log.e("AddAppointment", "Auth token is null. Unable to fetch users.");
        }
    }

    // Logic for Submit button
    private void handleSubmit() {
        String appointmentTitle = etAppointmentTitle.getText().toString();
        String appointmentDescription = etAppointmentDescription.getText().toString();
        String appointmentType = spinnerAppointmentType.getSelectedItem().toString();
        String appointmentAbout = spinnerAppointmentAbout.getSelectedItem().toString();

        // Basic validation
        if (appointmentTitle.isEmpty() || appointmentDescription.isEmpty()) {
            Toast.makeText(AddAppointment_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("AddAppointment", "Title: " + appointmentTitle);
        Log.d("AddAppointment", "Description: " + appointmentDescription);
        Log.d("AddAppointment", "Type: " + appointmentType);
        Log.d("AddAppointment", "About: " + appointmentAbout);

        // Submit appointment (implementation pending)
        submitAppointment(appointmentTitle, appointmentDescription, appointmentType, appointmentAbout);

        Toast.makeText(AddAppointment_Activity.this, "Appointment Submitted", Toast.LENGTH_SHORT).show();
        finish(); // Close activity after submission
    }

    // Placeholder method for submitting appointment details
    private void submitAppointment(String title, String description, String type, String about) {
        // Implement your API call logic to submit the appointment here
    }

    // Logic for Cancel button
    private void handleCancel() {
        Toast.makeText(AddAppointment_Activity.this, "Appointment Canceled", Toast.LENGTH_SHORT).show();
        finish(); // Close activity on cancel
    }

    // Method to fetch TeleMed data based on search query
    private void fetchTeleMedData(String query, String authToken) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<TeleMedSearch>> call = apiService.searchTeleMed("Bearer " + authToken);

        call.enqueue(new Callback<List<TeleMedSearch>>() {
            @Override
            public void onResponse(Call<List<TeleMedSearch>> call, Response<List<TeleMedSearch>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teleMedSearchList = response.body();
                    Log.d("TeleMedSearch", "Fetched Data: " + teleMedSearchList);
                    adapter.updateData(teleMedSearchList); // Update adapter with fetched data
                } else {
                    Log.e("TeleMedSearch", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TeleMedSearch>> call, Throwable t) {
                Log.e("TeleMedSearch", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    // Set up SearchView functionality
    private void setupSearch() {
        searchView.setOnClickListener(v -> {
            recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView on SearchView click
            if (authToken != null) {
                fetchTeleMedData("", authToken); // Fetch all users when clicked
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchTeleMedData(query, authToken); // Fetch data based on query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchTeleMedData(newText, authToken); // Fetch data as text changes
                return false;
            }
        });
    }
}
