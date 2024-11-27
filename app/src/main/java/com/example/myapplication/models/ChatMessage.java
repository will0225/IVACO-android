package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage {
    private String username;
    private List<String> messages;

    public ChatMessage(String username) {
        this.username = username;
        this.messages = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getLastMessage() {
        return messages.get(messages.size() - 1);
    }

    public List<String> getPreviousMessages() {
        // Return all but the last message
        return messages.subList(0, messages.size() - 1);
    }
}
