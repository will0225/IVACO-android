package com.example.myapplication.models;

public class LoginResponse {
    private String token;
    private String message;
    private String id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String username;
    private String role;
    private String uri;
    private boolean active;
    private String organization_id;
    private boolean isPincode;


    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getFirst_name() {return first_name;}
    public void setFirst_name(String first_name) {this.first_name = first_name;}

    public String getMiddle_name() {return middle_name;}
    public void setMiddle_name(String middle_name) {this.middle_name = middle_name;}

    public String getLast_name() {return last_name;}
    public void setLast_name(String last_name) {this.last_name = last_name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPhone_number() {return phone_number;}
    public void setPhone_number(String phone_number) {this.phone_number = phone_number;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    public String getUri() {return uri;}
    public void setUri(String uri) {this.uri = uri;}

    public boolean isActive() {return active;}
    public void setActive(boolean active) {this.active = active;}

    public String getOrganization_id() {return organization_id;}
    public void setOrganization_id(String organization_id) {this.organization_id = organization_id;}

    public boolean isPincode() {return isPincode;}
    public void setPincode(boolean isPincode) {this.isPincode = isPincode;}

}


