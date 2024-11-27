package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Measurement {


    @SerializedName("title")
    private String title;

    @SerializedName("mode")
    private String mode;

    @SerializedName("type")
    private String type;

    @SerializedName("values")
    private List<Double> values;

    @SerializedName("unit")
    private String unit;

    @SerializedName("reading_source")
    private String readingSource;

    @SerializedName("doctor")
    private Doctor doctor;

    @SerializedName("nurse")
    private Nurse nurse;

    // Constructor
    public Measurement(String title,String mode, String type,
                       List<Double> values, String unit, String readingSource,
                       Doctor doctor, Nurse nurse) {

        this.title = title;
        this.mode = mode;
        this.type = type;
        this.values = values;
        this.unit = unit;
        this.readingSource = readingSource;
        this.doctor = doctor;
        this.nurse = nurse;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
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

    // Nested Doctor class
    public static class Doctor {
        @SerializedName("user")
        private User user;

        public Doctor(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    // Nested User class
    public static class User {
        @SerializedName("id")
        private String id;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("middle_name")
        private String middleName;

        @SerializedName("last_name")
        private String lastName;

        public User(String id, String firstName, String middleName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    // Nested Nurse class (empty for now)
    public static class Nurse {
        // Define any attributes or methods as needed for the Nurse class
    }
}
