package com.example.myapplication.adapter;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.JoinMeetingActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.TeleMedAppointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<TeleMedAppointment> appointmentList;

    public AppointmentAdapter(Context context, List<TeleMedAppointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        TeleMedAppointment appointment = appointmentList.get(position);

        // Bind appointment data to the views
        holder.tvAppointmentTitle.setText(appointment.getTitle());
        holder.tvAppointmentDescription.setText(appointment.getDescription());
        holder.tvAppointmentType.setText(appointment.getType());
        holder.tvAbout.setText(appointment.getAbout());
        holder.tvStartDate.setText(appointment.getStartDate());
        holder.tvEndDate.setText(appointment.getEndDate());

        // Assuming participant details are available
        if (appointment.getParticipants() != null && !appointment.getParticipants().isEmpty()) {
            TeleMedAppointment.Participant participant = appointment.getParticipants().get(0);
            holder.tvUsername.setText(participant.getUser().getFirstName() + " " + participant.getUser().getLastName());
            holder.tvRole.setText(participant.getUser().getRole());
            holder.tvStatus.setText(participant.getStatus());
        }

        // Handle "View" button click
        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, JoinMeetingActivity.class);
            intent.putExtra("appointment", appointment); // Pass the appointment object including ID
            context.startActivity(intent); // Start the JoinMeetingActivity
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppointmentTitle, tvAppointmentDescription, tvAppointmentType, tvAbout, tvStartDate, tvEndDate, tvUsername, tvRole, tvStatus;
        ImageView imgUser;
        Button btnView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppointmentTitle = itemView.findViewById(R.id.appointment_title);
            tvAppointmentDescription = itemView.findViewById(R.id.appointment_description);
            tvAppointmentType = itemView.findViewById(R.id.appointment_type);
            tvAbout = itemView.findViewById(R.id.appointment_about);
            tvStartDate = itemView.findViewById(R.id.start_date);
            tvEndDate = itemView.findViewById(R.id.end_date);
            tvUsername = itemView.findViewById(R.id.first_name);
            tvRole = itemView.findViewById(R.id.role);
            tvStatus = itemView.findViewById(R.id.status);
            imgUser = itemView.findViewById(R.id.uri);
            btnView = itemView.findViewById(R.id.btn_view);
        }
    }
}
