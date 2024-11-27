package com.example.myapplication.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PairedDevicesAdapter extends RecyclerView.Adapter<PairedDevicesAdapter.DeviceViewHolder> {
    private List<BluetoothDevice> devicesList;

    public PairedDevicesAdapter(List<BluetoothDevice> devicesList) {
        this.devicesList = devicesList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each device item
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        // Get the Bluetooth device from the list
        BluetoothDevice device = devicesList.get(position);
        // Set the device name in the TextView
        holder.deviceName.setText(device.getName());
    }

    @Override
    public int getItemCount() {
        // Return the size of the devices list
        return devicesList.size();
    }

    // ViewHolder class for each device item
    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextView for the device name
            deviceName = itemView.findViewById(android.R.id.text1);
        }
    }
}
