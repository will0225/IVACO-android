package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.TeleMedSearch;

import java.util.List;

public class TeleMedSearchAdapter extends RecyclerView.Adapter<TeleMedSearchAdapter.ViewHolder> {

    private Context context;
    private List<TeleMedSearch> teleMedSearchList;

    public TeleMedSearchAdapter(Context context, List<TeleMedSearch> teleMedSearchList) {
        this.context = context;
        this.teleMedSearchList = teleMedSearchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_participants, parent, false); // Replace with your item layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeleMedSearch teleMedSearch = teleMedSearchList.get(position);
        holder.textViewName.setText(teleMedSearch.getFirstName() + " " + teleMedSearch.getLastName());
        holder.textViewEmail.setText(teleMedSearch.getEmail());
    }

    @Override
    public int getItemCount() {
        return teleMedSearchList.size();
    }

    public void updateData(List<TeleMedSearch> newData) {
        teleMedSearchList.clear();
        teleMedSearchList.addAll(newData);
        notifyDataSetChanged(); // Notify the adapter about data changes
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName); // Replace with your TextView ID
            textViewEmail = itemView.findViewById(R.id.textViewEmail); // Replace with your TextView ID
        }
    }
}
