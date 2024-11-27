package com.example.myapplication.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class AvailableDevicesAdapter extends RecyclerView.Adapter<AvailableDevicesAdapter.ViewHolder> {
    private List<BluetoothDevice> availableDevices;
    private OnDeviceClickListener listener;

    // Constructor
    public AvailableDevicesAdapter(List<BluetoothDevice> availableDevices, OnDeviceClickListener listener) {
        this.availableDevices = availableDevices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDevice device = availableDevices.get(position);
        holder.bind(device, listener);
    }

    @Override
    public int getItemCount() {
        return availableDevices.size();
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(BluetoothDevice device);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceNameTextView;
        private TextView deviceAddressTextView; // Optional for displaying the address

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.device_name_text_view); // Change to your actual view ID
            deviceAddressTextView = itemView.findViewById(R.id.device_address_text_view); // Optional
        }

        public void bind(BluetoothDevice device, OnDeviceClickListener listener) {
            deviceNameTextView.setText(device.getName() != null ? device.getName() : "Unknown Device");
            deviceAddressTextView.setText(device.getAddress()); // Optional

            // Set the click listener for the entire item
            itemView.setOnClickListener(v -> listener.onDeviceClick(device));
        }
    }
}
