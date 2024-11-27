package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.ChatRoom;
import com.example.myapplication.models.SearchUserChat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout messagesContainer;
    private String userId;
    private String authToken;
    private SearchView searchView;
    private CardView searchResultBox; // CardView for search results
    private RecyclerView resultsRecyclerView; // RecyclerView to show the list of users
    private SearchUserAdapter userAdapter; // Adapter for RecyclerView
    private List<SearchUserChat.Chat_User> allUsers = new ArrayList<>(); // List to store all users

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); // Make sure this matches your XML layout file

        messagesContainer = findViewById(R.id.messagesContainer);
        searchView = findViewById(R.id.searchView); // Initialize SearchView
        searchResultBox = findViewById(R.id.searchResultBox); // Initialize CardView for search results
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView); // Initialize RecyclerView inside CardView

        // Setup RecyclerView with a linear layout manager and adapter
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new SearchUserAdapter(new ArrayList<>()); // Initialize with empty list
        resultsRecyclerView.setAdapter(userAdapter);

        // Retrieve user ID and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", null);
        authToken = sharedPreferences.getString("token", null);

        // Set up the search functionality
        setupSearchView();

        // Initial fetch of all users (empty query)
        if (authToken != null) {
            fetchUsers(" ", authToken); // Fetch all users initially
        } else {
            Log.e("ChatActivity", "Auth token is null. Unable to fetch users.");
        }

        // Fetch chat room data
        if (authToken != null) {
            fetchChatRoomData(authToken);
        } else {
            Log.e("ChatActivity", "Auth token is null. Unable to fetch chat room data.");
        }
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch filtered users when the search is submitted
                if (authToken != null) {
                    filterUsers(query); // Filter users based on the search query
                } else {
                    Log.e("ChatActivity", "Auth token is null. Unable to fetch users.");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter users as the text is being typed
                filterUsers(newText);
                return false;
            }
        });

        // Make the search results box visible when the SearchView is tapped
        searchView.setOnSearchClickListener(v -> searchResultBox.setVisibility(View.VISIBLE));

        // Hide the search results box when the SearchView is closed
        searchView.setOnCloseListener(() -> {
            searchResultBox.setVisibility(View.GONE);
            return false;
        });
    }

    private void fetchUsers(String query, String token) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<SearchUserChat.Chat_User>> call = apiService.searchUsers(query, "Bearer " + token);

        call.enqueue(new Callback<List<SearchUserChat.Chat_User>>() {
            @Override
            public void onResponse(Call<List<SearchUserChat.Chat_User>> call, Response<List<SearchUserChat.Chat_User>> response) {
                Log.d("API Response", "Code: " + response.code() + ", Message: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    allUsers.clear(); // Clear previous user list
                    allUsers.addAll(response.body()); // Store all users
                    filterUsers(""); // Initially display all users
                } else {
                    Log.e("SearchUserChat", "No users found or response is not successful.");
                }
            }

            @Override
            public void onFailure(Call<List<SearchUserChat.Chat_User>> call, Throwable t) {
                Log.e("SearchUserChat", "Failed to fetch users: " + t.getMessage());
            }
        });
    }

    private void fetchChatRoomData(String token) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<ChatRoom>> call = apiService.getChatRooms("Bearer " + token);

        call.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChatRoom> chatRooms = response.body();

                    // Clear the container before adding new data
                    messagesContainer.removeAllViews();

                    // Loop through each chat room and log details
                    for (ChatRoom chatRoom : chatRooms) {
                        Log.d("ChatRoom", "Chat Room ID: " + chatRoom.getId());

                        for (ChatRoom.User user : chatRoom.getUsers()) {
                            // Log user details
                            Log.d("ChatRoom.User", "First Name: " + user.getFirstName());
                            Log.d("ChatRoom.User", "Last Name: " + user.getLastName());
                            Log.d("ChatRoom.User", "Middle Name: " + user.getMiddleName());
                            Log.d("ChatRoom.User", "Email: " + user.getEmail());
                            Log.d("ChatRoom.User", "Role: " + user.getRole());
                            Log.d("ChatRoom.User", "URI: " + user.getUri());

                            // Inflate the room_list_xml layout
                            View userView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.room_list_xml, messagesContainer, false);

                            // Populate user data into the inflated layout
                            TextView usernameTextView = userView.findViewById(R.id.Username);
                            TextView emailTextView = userView.findViewById(R.id.Role);

                            // Set user data
                            usernameTextView.setText(user.getFirstName() + " " + user.getLastName());
                            emailTextView.setText(user.getEmail());

                            // Set an OnClickListener to navigate to ChatDetailActivity
                            userView.setOnClickListener(v -> {
                                Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                                // Pass user details to the next activity
                                intent.putExtra("firstName", user.getFirstName());
                                intent.putExtra("lastName", user.getLastName());
                                intent.putExtra("middleName", user.getMiddleName());
                                intent.putExtra("email", user.getEmail());
                                intent.putExtra("role", user.getRole());
                                intent.putExtra("uri", user.getUri());
                                intent.putExtra("chatRoomId", chatRoom.getId()); // Also passing chat room ID
                                startActivity(intent);
                            });

                            // Add the user view to the messagesContainer
                            messagesContainer.addView(userView);
                        }
                    }
                } else {
                    Log.e("ChatRoom", "Failed to fetch chat room data or response is not successful.");
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                Log.e("ChatRoom", "Failed to fetch chat room data: " + t.getMessage());
            }
        });
    }

    private void filterUsers(String query) {
        List<SearchUserChat.Chat_User> filteredUsers = new ArrayList<>();
        for (SearchUserChat.Chat_User user : allUsers) {
            if (user.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getLastName().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        userAdapter.updateUsers(filteredUsers); // Update the RecyclerView with the filtered users
    }

    // RecyclerView Adapter for displaying search results
    private class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.UserViewHolder> {
        private List<SearchUserChat.Chat_User> userList;

        SearchUserAdapter(List<SearchUserChat.Chat_User> userList) {
            this.userList = userList;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_xml, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            SearchUserChat.Chat_User user = userList.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public void updateUsers(List<SearchUserChat.Chat_User> users) {
            this.userList = users;
            notifyDataSetChanged();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            private TextView messageRole;
            private TextView messageUsername;
            private ImageView messageUserImage;

            UserViewHolder(View itemView) {
                super(itemView);
                messageRole = itemView.findViewById(R.id.messageRole);
                messageUsername = itemView.findViewById(R.id.messageUsername);
                messageUserImage = itemView.findViewById(R.id.messageUserImage);
            }

            void bind(SearchUserChat.Chat_User user) {
                messageRole.setText(user.getRole());
                messageUsername.setText(user.getFirstName());
                messageUserImage.setImageResource(R.drawable.baseline_person_24);

                // Set a click listener to open ChatDetailActivity with user info
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                    intent.putExtra("FIRST_NAME", user.getFirstName());
                    intent.putExtra("LAST_NAME", user.getLastName());
                    intent.putExtra("MIDDLE_NAME", user.getMiddleName());
                    intent.putExtra("USER_EMAIL", user.getEmail());
                    intent.putExtra("USER_ID", user.getChatUserId());
                    intent.putExtra("USER_Uri", user.getUri());
                    intent.putExtra("USER_ROLE", user.getRole());
                    intent.putExtra("USER_IMAGE", R.drawable.baseline_person_24);
                    startActivity(intent);
                });
            }
        }
    }
}