package com.example.myapplication.api;

//import com.example.myapplication.models.ChatResponse;
import com.example.myapplication.models.ChatRoom;
import com.example.myapplication.models.Education;
import com.example.myapplication.models.JoinMeetingResponse;
import com.example.myapplication.models.JoinedMeetingProps;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.models.Measurement;
import com.example.myapplication.models.SearchUserChat;
import com.example.myapplication.models.TeleMedAppointment;
import com.example.myapplication.models.TeleMedSearch;
import com.example.myapplication.models.UserMeasurement;
import com.example.myapplication.models.UserProfile;
import com.example.myapplication.models.ForgotPasswordRequest;
import com.example.myapplication.models.ForgotPasswordResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/v1/web/auth/login") // Actual login endpoint
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Add this method for fetching user profile data
    @GET("api/v1/web/patient/profile") // Removed leading slash
    Call<UserProfile> getUserProfile(@Header("Authorization") String token);

    // Forgot password endpoint
    @POST("api/v1/web/auth/forgot-password") // Adjust this based on your actual endpoint
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @GET("api/v1/web/chat/search/users")
    Call<List<SearchUserChat.Chat_User>> searchUsers(@Query("query") String query, @Header("Authorization") String token);

    // ApiService.java
    @GET("api/v1/web/chat/room")  // Make sure this is the correct endpoint
    Call<List<ChatRoom>> getChatRooms(@Header("Authorization") String token);

//    @GET("api/v1/web/chat/room/{roomId}")
//    Call<ChatResponse> getChats(
//            @Path("roomId") String roomId,
//            @Header("Authorization") String token
//    );
    @GET("/api/v1/web/measurements/patients/{patientId}")
    Call<List<UserMeasurement>> getMeasurements(
            @Path("patientId") String patientId,
            @Header("Authorization") String token);


    @POST("/api/v1/web/measurements/patients/{patientId}")
    Call<Measurement> sendMeasurement(
            @Path("patientId") String patientId,
            @Header("Authorization") String token,
            @Body Measurement measurement);



    @GET("/api/v1/web/education")
    Call<List<Education>> getEducationData(@Header("Authorization") String token);

    @GET("/api/v1/web/telemed/search")
    Call<List<TeleMedSearch>> searchTeleMed(@Header("Authorization") String token);

    @GET("/api/v1/web/telemed")
    Call<List<TeleMedAppointment>> getTeleMedAppointment(@Header("Authorization") String token);

    @GET("/api/v1/web/telemed/view/{id}")
    Call<TeleMedAppointment> getAppointmentDetails(@Header("Authorization") String token, @Path("id") String appointmentId);

    @GET("/join-meeting/{id}")
    Call<JoinedMeetingProps> joinMeeting(@Path("id") String id);

}
