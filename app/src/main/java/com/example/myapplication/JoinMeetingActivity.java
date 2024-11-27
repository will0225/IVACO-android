package com.example.myapplication;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.JoinMeetingResponse;
import com.example.myapplication.models.TeleMedAppointment; // Import your model class

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinMeetingActivity extends AppCompatActivity {

    private Button btnJoin;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_meeting);

        // Initialize views (including btnJoin)
        initializeViews();

        // Retrieve the appointment data from the Intent
        TeleMedAppointment appointment = (TeleMedAppointment) getIntent().getSerializableExtra("appointment");

        // Log the appointment data
        if (appointment != null) {
            Log.d("JoinMeetingActivity", "Appointment Id: " + appointment.getId());
            Log.d("JoinMeetingActivity", "Appointment Title: " + appointment.getTitle());
            Log.d("JoinMeetingActivity", "Appointment Description: " + appointment.getDescription());
            Log.d("JoinMeetingActivity", "Appointment Type: " + appointment.getType());
            Log.d("JoinMeetingActivity", "Appointment About: " + appointment.getAbout());
            Log.d("JoinMeetingActivity", "Start Date: " + appointment.getStartDate());
            Log.d("JoinMeetingActivity", "End Date: " + appointment.getEndDate());

            // Set the text for each TextView
            ((TextView) findViewById(R.id.tvAppointmentId)).setText(appointment.getId());
            ((TextView) findViewById(R.id.tvAppointmentTitle)).setText(appointment.getTitle());
            ((TextView) findViewById(R.id.tvAppointmentDescription)).setText(appointment.getDescription());
            ((TextView) findViewById(R.id.tvAppointmentType)).setText(appointment.getType());
            ((TextView) findViewById(R.id.tvAbout)).setText(appointment.getAbout());
            ((TextView) findViewById(R.id.tvStartDate)).setText(appointment.getStartDate());
            ((TextView) findViewById(R.id.tvEndDate)).setText(appointment.getEndDate());

            // Log participant details
            if (appointment.getParticipants() != null && !appointment.getParticipants().isEmpty()) {
                TeleMedAppointment.Participant participant = appointment.getParticipants().get(0);
                Log.d("JoinMeetingActivity", "Participant Name: " + participant.getUser().getFirstName() + " " + participant.getUser().getLastName());
                Log.d("JoinMeetingActivity", "Participant Role: " + participant.getUser().getRole());
                Log.d("JoinMeetingActivity", "Participant Status: " + participant.getStatus());
            }

            // Store the appointment ID in a member variable for later use
            final String appointmentId = appointment.getId();

            // Set OnClickListener for Join button
            joinMeeting(appointment.getId());
            btnJoin.setOnClickListener(view -> {
                Log.d(TAG, "Join Meeting button clicked");
                Intent intent = new Intent(JoinMeetingActivity.this, VideoCall_Activity.class);
                // Pass the appointment ID to the next activity
                intent.putExtra("appointmentId", appointmentId);
                startActivity(intent);
            });

            // Join the meeting using the appointment ID
            joinMeeting(appointment.getId());
        } else {
            Log.e("JoinMeetingActivity", "No appointment data found in the intent.");
            Toast.makeText(this, "No appointment data available.", Toast.LENGTH_SHORT).show();
        }
    }

    // Initialize the button and any other views
    private void initializeViews() {
        btnJoin = findViewById(R.id.btn_join);
        Log.d(TAG, "Views initialized in initializeViews");
    }

    private void joinMeeting(String meetingId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("token", null);

        if (authToken == null) {
            Log.e("JoinMeetingActivity", "Token not found, cannot proceed with joining the meeting.");
            Toast.makeText(JoinMeetingActivity.this, "Authorization token missing.", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
