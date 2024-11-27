package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Education {

    @SerializedName("id")
    private String id;

    @SerializedName("uri")
    private String uri;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("author")
    private Author author;

    @SerializedName("likes_count")
    private int likesCount;

    @SerializedName("views_count")
    private int viewsCount;

    @SerializedName("created_at")
    private Date createdAt;

    // Default constructor for deserialization
    public Education() {}

    // Parameterized constructor
    public Education(String id, String uri, String title, String content, Author author, int likesCount, int viewsCount, Date createdAt) {
        this.id = id;
        this.uri = uri;
        this.title = title;
        this.content = content;
        this.author = author;
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id != null ? id : "Unknown";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri != null ? uri : "";
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title != null ? title : "No Title";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content != null ? content : "No Content Available";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author != null ? author : new Author("Unknown", "Author");
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Date getCreatedAt() {
        return createdAt != null ? createdAt : new Date(); // Returns current date if null
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Nested Author class
    public static class Author {

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("last_name")
        private String lastName;

        // Default constructor for deserialization
        public Author() {}

        // Constructor with parameters
        public Author(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        // Getters and Setters
        public String getFirstName() {
            return firstName != null ? firstName : "Unknown";
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName != null ? lastName : "Author";
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
