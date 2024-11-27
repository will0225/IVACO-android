package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchUserChat {
    // If the API returns a list of users directly,
    // you might not need this wrapper class.
    // If you still want to use it for other responses, keep it.
    @SerializedName("users") // Optional: Use this if your JSON field is named "users"
    private List<Chat_User> users;

    public List<Chat_User> getUsers() {
        return users;
    }

    public void setUsers(List<Chat_User> users) {
        this.users = users;
    }

    public static class Chat_User {
        @SerializedName("id") // Map to JSON field "first_name"
        private String id;

        @SerializedName("first_name") // Map to JSON field "first_name"
        private String firstName;

        @SerializedName("last_name") // Map to JSON field "last_name"
        private String lastName;

        @SerializedName("middle_name") // Map to JSON field "last_name"
        private String middleName;

        @SerializedName("email") // Map to JSON field "email"
        private String email;

        @SerializedName("role") // Map to JSON field "role"
        private String role;

        @SerializedName("uri") // Map to JSON field "uri"
        private String uri; // Field to hold the URI

        // Getters and Setters for each field
        public String getChatUserId() { return id; }
        public void setChatUserId(String id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getMiddleName() { return middleName; }
        public void setMiddleName(String middleName) { this.middleName = middleName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getUri() { return uri; }
        public void setUri(String uri) { this.uri = uri; }
    }
}
