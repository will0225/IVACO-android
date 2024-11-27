package com.example.myapplication.models;

public class TeleMedSearch {
    private String email;
    private String first_name;
    private String last_name;
    private String middle_name;
    private String id;

    // Constructor, getters, and setters

    public TeleMedSearch(String email, String first_name, String last_name, String middle_name, String id) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getMiddleName() {
        return middle_name;
    }

    public String getId() {
        return id;
    }

    // Override toString method
    @Override
    public String toString() {
        return "TeleMedSearch{" +
                "email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", middle_name='" + (middle_name != null ? middle_name : "N/A") + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
