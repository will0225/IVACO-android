package com.example.myapplication.models;


import java.util.List;

public class MeetingDetails {
    private String title;
    private List<TeleMedAppointment.Participant> participants;
    private String meetingLink;

    // Getters
    public String getTitle() {
        return title;
    }

    public List<TeleMedAppointment.Participant> getParticipants() {
        return participants;
    }

    public String getMeetingLink() {
        return meetingLink;
    }
}
