package com.example.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler; // Import the Handler class
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.AvailableDevicesAdapter;

import java.util.ArrayList;
import java.util.List;

public class Device_Activity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_CONNECT = 1;
    private static final int REQUEST_BLUETOOTH_SCAN = 2;
    private TextView deviceNameTextView;
    private TextView connectionStatusTextView;
    private Switch bluetoothToggle;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver bluetoothStateReceiver;
    private RecyclerView availableDevicesRecyclerView;
    private AvailableDevicesAdapter availableDevicesAdapter;
    private List<BluetoothDevice> availableDevices;

    private boolean isBluetoothReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        deviceNameTextView = findViewById(R.id.device_name);
        connectionStatusTextView = findViewById(R.id.connection_status);
        bluetoothToggle = findViewById(R.id.bluetooth_toggle);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        availableDevicesRecyclerView = findViewById(R.id.available_devices_recycler_view);
        availableDevicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableDevices = new ArrayList<>();
        availableDevicesAdapter = new AvailableDevicesAdapter(availableDevices, this::showConnectionDialog);
        availableDevicesRecyclerView.setAdapter(availableDevicesAdapter);

        if (bluetoothAdapter == null) {
            deviceNameTextView.setText("Bluetooth is not supported on this device.");
            return;
        }

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action != null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_ON) {
                        displayBluetoothDeviceName();
                        checkBluetoothConnection(false); // Initially set to false
                        startDiscovery(); // Start discovering devices
                    } else if (state == BluetoothAdapter.STATE_OFF) {
                        deviceNameTextView.setText("Bluetooth is not enabled.");
                        connectionStatusTextView.setText("Not Connected");
                        availableDevices.clear();
                        availableDevicesAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        // Inside onCreate method
        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> onBackPressed());
        registerReceiver(bluetoothStateReceiver, filter);
        isBluetoothReceiverRegistered = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        }, REQUEST_BLUETOOTH_CONNECT);
            } else {
                setupBluetoothToggle();
                displayBluetoothDeviceName();
                checkBluetoothConnection(false); // Initially set to false
            }
        } else {
            setupBluetoothToggle();
            displayBluetoothDeviceName();
            checkBluetoothConnection(false); // Initially set to false
        }
    }

    private void startDiscovery() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        Log.d("Discovery", "Starting Bluetooth discovery...");
        availableDevices.clear(); // Clear previous devices
        availableDevicesAdapter.notifyDataSetChanged(); // Notify adapter about the change
        bluetoothAdapter.startDiscovery();

        // Register receiver for discovered devices
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(deviceFoundReceiver, filter);
    }

    private BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !availableDevices.contains(device)) { // Check for duplicates
                    Log.d("Device Found", "Found device: " + device.getName() + " - " + device.getAddress());
                    availableDevices.add(device);
                    availableDevicesAdapter.notifyItemInserted(availableDevices.size() - 1);
                }
            }
        }
    };

    private void checkBluetoothConnection(boolean isConnected) {
        if (isConnected) {
            connectionStatusTextView.setText("Connected");
            connectionStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            connectionStatusTextView.setText("Not Connected");
            connectionStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void setupBluetoothToggle() {
        bluetoothToggle.setChecked(bluetoothAdapter.isEnabled());

        bluetoothToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.enable();
                        displayBluetoothDeviceName();
                    }
                } else {
                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                    }
                }
                checkBluetoothConnection(false); // Reset connection status on toggle change
            }
        });
    }

    private void displayBluetoothDeviceName() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            String deviceName = bluetoothAdapter.getName();
            if (deviceName != null) {
                deviceNameTextView.setText("Device Name: " + deviceName);
            } else {
                deviceNameTextView.setText("Device Name: Unknown");
            }
        } else {
            deviceNameTextView.setText("Bluetooth is not enabled.");
        }
    }

    private void showConnectionDialog(BluetoothDevice device) {
        new AlertDialog.Builder(this)
                .setTitle("Connect to " + device.getName())
                .setMessage("Do you want to connect to this device?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Show pairing message for 5 seconds
                    connectionStatusTextView.setText("Pairing...");
                    connectionStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

                    // Simulate pairing delay
                    new Handler().postDelayed(() -> {
                        // After 5 seconds, fake the connection
                        checkBluetoothConnection(true);
                        Toast.makeText(this, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
                    }, 5000); // 5000 milliseconds = 5 seconds
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0) {
                boolean bluetoothConnectGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean bluetoothScanGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (bluetoothConnectGranted && bluetoothScanGranted) {
                    setupBluetoothToggle();
                    displayBluetoothDeviceName();
                    checkBluetoothConnection(false); // Initially set to false
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBluetoothReceiverRegistered) {
            unregisterReceiver(bluetoothStateReceiver);
            isBluetoothReceiverRegistered = false;
        }

        // Only unregister deviceFoundReceiver if it was registered
        try {
            unregisterReceiver(deviceFoundReceiver); // Unregister device found receiver
        } catch (IllegalArgumentException e) {
            Log.e("Device_Activity", "Receiver not registered: " + e.getMessage());
        }
    }

}
