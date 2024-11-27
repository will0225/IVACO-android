package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class VideoCall_Activity extends AppCompatActivity {

    private String appId = "cc4665544d34477b93f1783e64560ff9"; // Your Agora App ID
    private String channelName; // Channel name set from appointmentId
    private String token = null; // Token can be set here if needed

    private RtcEngine mRtcEngine;
    private boolean isCameraEnabled = true; // Track camera state
    private boolean isMicEnabled = true; // Track microphone state
    private boolean isSpeakerEnabled = true; // Track speaker state

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            runOnUiThread(() -> Toast.makeText(VideoCall_Activity.this, "Join channel success", Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(() -> setupRemoteVideo(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(() -> Toast.makeText(VideoCall_Activity.this, "User offline: " + uid, Toast.LENGTH_SHORT).show());
        }
    };

    private static final int PERMISSION_REQ_ID = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        // Retrieve appointmentId from the Intent
        String appointmentId = getIntent().getStringExtra("appointmentId");
        if (appointmentId != null) {
            channelName = appointmentId;
        }

        // Check permissions and initialize Agora engine
        if (checkPermissions()) {
            initializeAgoraEngine();
        } else {
            ActivityCompat.requestPermissions(this, getRequiredPermissions(), PERMISSION_REQ_ID);
        }

        // Set up button listeners
        setupButtonListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure permissions are granted and join the channel
        if (checkPermissions()) {
            initializeAndJoinChannel();
        }
    }

    private void initializeAgoraEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            mRtcEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Agora SDK initialization failed: " + e.getMessage());
        }
        mRtcEngine.enableVideo();
        mRtcEngine.startPreview();
    }

    private void initializeAndJoinChannel() {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));

        ChannelMediaOptions options = new ChannelMediaOptions();
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;

        if (mRtcEngine != null) {
            mRtcEngine.joinChannel(token, channelName, 0, options);
        } else {
            Toast.makeText(this, "Agora engine is not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private String[] getRequiredPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        } else {
            return new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            };
        }
    }

    private boolean checkPermissions() {
        for (String permission : getRequiredPermissions()) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void setupButtonListeners() {
        // Camera Toggle Button
        CardView cameraButton = findViewById(R.id.card_camera);
        cameraButton.setOnClickListener(v -> {
            isCameraEnabled = !isCameraEnabled;
            mRtcEngine.muteLocalVideoStream(!isCameraEnabled);
            Toast.makeText(this, isCameraEnabled ? "Camera Enabled" : "Camera Disabled", Toast.LENGTH_SHORT).show();
        });

        // Microphone Toggle Button
        CardView micButton = findViewById(R.id.card_mic);
        micButton.setOnClickListener(v -> {
            isMicEnabled = !isMicEnabled;
            mRtcEngine.muteLocalAudioStream(!isMicEnabled);
            Toast.makeText(this, isMicEnabled ? "Microphone Enabled" : "Microphone Disabled", Toast.LENGTH_SHORT).show();
        });

        // Speaker Toggle Button
        CardView speakerButton = findViewById(R.id.card_speaker);
        speakerButton.setOnClickListener(v -> {
            isSpeakerEnabled = !isSpeakerEnabled;
            mRtcEngine.setEnableSpeakerphone(isSpeakerEnabled);
            Toast.makeText(this, isSpeakerEnabled ? "Speaker Enabled" : "Speaker Disabled", Toast.LENGTH_SHORT).show();
        });

        // End Call Button
        CardView endCallButton = findViewById(R.id.card_end_call);
        endCallButton.setOnClickListener(v -> {
            mRtcEngine.leaveChannel();
            finish(); // Close the activity
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermissions()) {
            initializeAgoraEngine();
            initializeAndJoinChannel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRtcEngine != null) {
            mRtcEngine.stopPreview();
            mRtcEngine.leaveChannel();
        }
    }
}
