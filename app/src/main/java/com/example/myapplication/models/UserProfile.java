package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("first_name") // Ensure these match the JSON response
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String dob;
    private String height;
    private String weight;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String username;
    private String language;
    private String time;
    private String ssn;
    private String subscriber;
    private String measurement_system;
    private String email;  // Add email field

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSocialSecurityNumber() { return ssn; }
    public void setSocialSecurityNumber(String ssn) { this.ssn = ssn; }

    public String getsubscriber() { return subscriber; }
    public void setsubscriber(String subscriber) { this.subscriber = subscriber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getMeasurement_System() { return measurement_system; }
    public void setMeasurement_System(String measurement_system) { this.measurement_system = measurement_system; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }


    // Optionally, override toString for logging
    @Override
    public String toString() {
        return "UserProfile{" +
                ", username='" + username + '\'' +
                ", ssn='" + ssn + '\'' +
                ", subscriber='" + subscriber + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", language='" + language + '\'' +
                ", measurement_system='" + measurement_system + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

