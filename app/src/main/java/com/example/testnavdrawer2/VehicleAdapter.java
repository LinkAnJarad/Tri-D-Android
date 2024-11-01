package com.example.testnavdrawer2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<VehicleEntry> vehicleList;
    private Context context;

    public VehicleAdapter(Context context, List<VehicleEntry> vehicleList) {
        this.context = context;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_item, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        VehicleEntry vehicle = vehicleList.get(position);
        holder.textPlateNumber.setText(vehicle.getPlateNumber());
        holder.textType.setText(vehicle.getType());
        holder.textBrand.setText(vehicle.getBrand());
        holder.textColor.setText(vehicle.getColor());

        // Set verification status
        holder.imageVerified.setImageResource(
                vehicle.isVerified() ? R.drawable.ic_verified : R.drawable.ic_unverified
        );
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public void addVehicle(VehicleEntry vehicle) {
        vehicleList.add(vehicle);
        notifyItemInserted(vehicleList.size() - 1);
    }

    public void removeVehicle(int position) {
        if (position >= 0 && position < vehicleList.size()) {
            vehicleList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView textPlateNumber;
        TextView textType;
        TextView textBrand;
        TextView textColor;
        ImageView imageVerified;

        VehicleViewHolder(View itemView) {
            super(itemView);
            textPlateNumber = itemView.findViewById(R.id.textPlateNumber);
            textType = itemView.findViewById(R.id.textType);
            textBrand = itemView.findViewById(R.id.textBrand);
            textColor = itemView.findViewById(R.id.textColor);
            imageVerified = itemView.findViewById(R.id.imageVerified);
        }
    }
}