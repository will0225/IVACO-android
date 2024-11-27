package com.example.myapplication.models;

import java.util.List;

public class ChatRoom {
    private String id;
    private List<User> users;

    // Getters and setters for ChatRoom
    public String getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    // Inner User class
    public class User {
        private String first_name;
        private String last_name;
        private String middle_name;
        private String email;
        private String role;
        private String uri;

        // Getters and setters for User
        public String getFirstName() {
            return first_name;
        }

        public String getLastName() {
            return last_name;
        }

        public String getMiddleName() {
            return middle_name;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getUri() {
            return uri;
        }
    }
}


