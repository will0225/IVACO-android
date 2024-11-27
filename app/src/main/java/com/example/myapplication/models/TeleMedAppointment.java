package com.example.myapplication.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class TeleMedAppointment implements Serializable {

    private String id;
    private String title;
    private String description;
    private String type;
    private String about;
    private String start_date;
    private String end_date;
    private List<Participant> participants;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String startDate) {
        this.start_date = startDate;
    }

    public String getEndDate() {
        return end_date;
    }

    public void setEndDate(String endDate) {
        this.end_date = endDate;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    // Participant class
    public static class Participant implements Serializable {
        private User user;
        private String status;

        // Getters and Setters
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // User class
        public static class User implements Serializable {
            private String first_name;
            private String last_name;
            private String middle_name;
            private String role;
            private String uri;

            // Getters and Setters
            public String getFirstName() {
                return first_name;
            }

            public void setFirstName(String firstName) {
                this.first_name = firstName;
            }

            public String getLastName() {
                return last_name;
            }

            public void setLastName(String lastName) {
                this.last_name = lastName;
            }

            public String getMiddleName() {
                return middle_name;
            }

            public void setMiddleName(String middleName) {
                this.middle_name = middleName;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public Uri getUri() {
                return Uri.parse(uri); // Convert String to Uri
            }

            public void setUri(String uri) {
                this.uri = uri;
            }
        }
    }
}

