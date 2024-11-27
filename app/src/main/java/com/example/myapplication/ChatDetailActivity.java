package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
//import com.example.myapplication.models.ChatResponse;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private LinearLayout messagesContainer;
    private EditText messageEditText;
    private Button sendButton;
    private ScrollView messagesScrollView;
    private ImageButton attachmentButton;
    private String authToken;

    private String existingRoomId; // Store the room ID for fetching chats

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail); // Set the content view first

        // Initialize views
        messagesContainer = findViewById(R.id.messagesContainer);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messagesScrollView = findViewById(R.id.messagesScrollView);
        attachmentButton = findViewById(R.id.attachmentButton);
        ImageView userImageView = findViewById(R.id.userImageView);
        TextView usernameTextView = findViewById(R.id.usernameTextView);

        // Get user data from intent
        Intent intent = getIntent();
        if (intent != null) {
            existingRoomId = intent.getStringExtra("chatRoomId"); // Save the chat room ID
            Log.d("ChatDetailActivity", "Received chatRoomId: " + existingRoomId);

            // Display previous messages
            ArrayList<String> previousMessages = intent.getStringArrayListExtra("PREVIOUS_MESSAGES");
            if (previousMessages != null) {
                for (String message : previousMessages) {
                    addMessage(message, false);
                }
            } else {
                Log.d("ChatDetailActivity", "No previous messages found.");
            }
        }

        // Send button click listener
        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();
            if (!message.isEmpty()) {
                addMessage(message, true);
                messageEditText.setText("");
            } else {
                Log.d("ChatDetailActivity", "Attempted to send an empty message.");
            }
        });

        // Attachment button click listener
        attachmentButton.setOnClickListener(v -> openFilePicker());

        // Retrieve user ID and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("token", null);
        Log.d("ChatDetailActivity", "Retrieved token: " + (authToken != null ? "Token is available" : "Token is null"));

        // Fetch existing chats when activity is created
        fetchChats();
    }

     private void fetchChats() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Log.d("ChatDetailActivity", "Fetching chats for room ID: " + existingRoomId);
//        Call<ChatResponse> call = apiService.getChats(existingRoomId, "Bearer " + authToken);
//        call.enqueue(new Callback<ChatResponse>() {
//            @Override
//            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<ChatResponse.Chat> chats = response.body().getChats();
//                    Log.d("ChatDetailActivity", "Chats fetched successfully. Number of chats: " + chats.size());
//
//                    // Display each chat message in the messagesContainer
//                    for (ChatResponse.Chat chat : chats) {
//                        Log.d("ChatDetailActivity", "Sender: " + chat.getSender() + ", Message: " + chat.getMessage());
//                        addMessage(chat.getMessage(), chat.getSender().equals("your_user_id")); // Replace "your_user_id" with the actual sender ID of the logged-in user
//                    }
//
//                    // Scroll to the bottom of the message container after loading messages
//                    messagesScrollView.post(() -> messagesScrollView.fullScroll(View.FOCUS_DOWN));
//                } else {
//                    Log.e("ChatDetailActivity", "No chats found or response not successful. Response code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChatResponse> call, Throwable t) {
//                Log.e("ChatDetailActivity", "Failed to fetch chats: " + t.getMessage());
//            }
//        });
    }


    private void addMessage(String messageText, boolean isSent) {
        int layoutId = isSent ? R.layout.item_message_sent : R.layout.item_message_received;
        View messageView = LayoutInflater.from(this).inflate(layoutId, messagesContainer, false);
        TextView messageTextView = messageView.findViewById(R.id.messageText);
        messageTextView.setText(messageText);
        messagesContainer.addView(messageView);
        messagesScrollView.post(() -> messagesScrollView.fullScroll(View.FOCUS_DOWN));
        Log.d("ChatDetailActivity", "Added message: " + messageText + ", Sent: " + isSent);
    }

    private void addAttachmentMessage(String fileName) {
        View attachmentView = LayoutInflater.from(this).inflate(R.layout.item_message_attachment, messagesContainer, false);
        TextView attachmentFileNameText = attachmentView.findViewById(R.id.attachmentFileNameText);
        attachmentFileNameText.setText(fileName);
        messagesContainer.addView(attachmentView);
        messagesScrollView.post(() -> messagesScrollView.fullScroll(View.FOCUS_DOWN));
        Log.d("ChatDetailActivity", "Added attachment message: " + fileName);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allows all file types
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        Log.d("ChatDetailActivity", "File picker opened.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            String fileName = fileUri != null ? fileUri.getLastPathSegment() : "Unknown File";
            addAttachmentMessage(fileName);
            Toast.makeText(this, "File selected: " + (fileUri != null ? fileUri.getPath() : "Unknown"), Toast.LENGTH_SHORT).show();
            Log.d("ChatDetailActivity", "File selected: " + (fileUri != null ? fileUri.getPath() : "Unknown"));
        } else {
            Log.e("ChatDetailActivity", "File selection failed or canceled.");
        }
    }
}
