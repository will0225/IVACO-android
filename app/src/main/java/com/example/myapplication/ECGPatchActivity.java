package com.example.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.UUID;

public class ECGPatchActivity extends AppCompatActivity {
    private static final String TAG = "ECGPatchActivity";
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private TextView heartRateTextView, respiratoryRateTextView, ecgDataTextView;

    private final UUID ECG_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"); // Example UUID
    private final UUID HEART_RATE_CHARACTERISTIC_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"); // Example UUID
    private final UUID RESPIRATORY_CHARACTERISTIC_UUID = UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb"); // Example UUID
    private final UUID ECG_CHARACTERISTIC_UUID = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb"); // Example UUID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecgpatch);

        heartRateTextView = findViewById(R.id.heart_rate_value);
        respiratoryRateTextView = findViewById(R.id.respiratory_rate_value);
        ecgDataTextView = findViewById(R.id.ecg_data_value);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available on this device", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
        } else {
            startScan();
        }
    }

    private void startScan() {
        Log.d(TAG, "Starting BLE scan...");
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "Found device: " + device.getName() + " (" + device.getAddress() + ")");
            if (device.getName() != null && device.getName().contains("ECG Patch")) {
                bluetoothAdapter.stopLeScan(leScanCallback);
                connectToDevice(device);
            }
        }
    };

    private void connectToDevice(BluetoothDevice device) {
        Log.d(TAG, "Connecting to device: " + device.getName());
        bluetoothGatt = device.connectGatt(this, false, gattCallback);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Connected to device, discovering services...");
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Disconnected from device");
                runOnUiThread(() -> {
                    heartRateTextView.setText("Disconnected");
                    respiratoryRateTextView.setText("Disconnected");
                    ecgDataTextView.setText("No data");
                });
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService ecgService = gatt.getService(ECG_SERVICE_UUID);
                if (ecgService != null) {
                    BluetoothGattCharacteristic heartRateChar = ecgService.getCharacteristic(HEART_RATE_CHARACTERISTIC_UUID);
                    BluetoothGattCharacteristic respiratoryChar = ecgService.getCharacteristic(RESPIRATORY_CHARACTERISTIC_UUID);
                    BluetoothGattCharacteristic ecgChar = ecgService.getCharacteristic(ECG_CHARACTERISTIC_UUID);

                    if (heartRateChar != null) {
                        gatt.setCharacteristicNotification(heartRateChar, true);
                        readCharacteristic(heartRateChar);
                    }

                    if (respiratoryChar != null) {
                        gatt.setCharacteristicNotification(respiratoryChar, true);
                        readCharacteristic(respiratoryChar);
                    }

                    if (ecgChar != null) {
                        gatt.setCharacteristicNotification(ecgChar, true);
                        readCharacteristic(ecgChar);
                    }
                }
            } else {
                Log.e(TAG, "Service discovery failed, status: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                updateCharacteristicData(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            updateCharacteristicData(characteristic);
        }
    };

    private void updateCharacteristicData(BluetoothGattCharacteristic characteristic) {
        if (HEART_RATE_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            int heartRate = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            runOnUiThread(() -> heartRateTextView.setText(String.valueOf(heartRate) + " bpm"));
        } else if (RESPIRATORY_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            int respiratoryRate = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            runOnUiThread(() -> respiratoryRateTextView.setText(String.valueOf(respiratoryRate) + " brpm"));
        } else if (ECG_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            byte[] ecgData = characteristic.getValue();
            runOnUiThread(() -> ecgDataTextView.setText("ECG Data: " + bytesToHex(ecgData)));
        }
    }

    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt != null) {
            bluetoothGatt.readCharacteristic(characteristic);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
        } else {
            Toast.makeText(this, "Bluetooth permissions required", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
