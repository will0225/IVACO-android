package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserMeasurement {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("status")
    private String status;

    @SerializedName("mode")
    private String mode;

    @SerializedName("type")
    private String type;

    @SerializedName("values")
    private List<Integer> values;

    @SerializedName("unit")
    private String unit;

    @SerializedName("reading_source")
    private String readingSource;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("patient")
    private Patient patient;

    @SerializedName("doctor")
    private Doctor doctor; // This can be null

    @SerializedName("nurse")
    private Nurse nurse; // This can also be null

    // Getters and setters

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReadingSource() {
        return readingSource;
    }

    public void setReadingSource(String readingSource) {
        this.readingSource = readingSource;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    // Inner classes for Patient, Doctor, and Nurse

    public static class Patient {
        @SerializedName("user")
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class Doctor {
        @SerializedName("user")
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class Nurse {
        @SerializedName("user")
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class User {
        @SerializedName("id")
        private String id;

        @SerializedName("email")
        private String email;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("middle_name")
        private String middleName;

        @SerializedName("last_name")
        private String lastName;

        // Getters and setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
