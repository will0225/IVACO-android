package com.example.myapplication.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "https://mobility-health-backend-production.up.railway.app/";
    private static volatile Retrofit retrofit; // Use volatile for thread safety

    // Private constructor to prevent instantiation
    private RetrofitClient() {}

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) { // Synchronized block for thread-safe singleton
                if (retrofit == null) {
                    // Create a logging interceptor
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    // Build OkHttpClient with logging interceptor and custom timeouts
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
                            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
                            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
                            .build();

                    // Build Retrofit instance
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    // Optional: Method to create ApiService instance directly
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
