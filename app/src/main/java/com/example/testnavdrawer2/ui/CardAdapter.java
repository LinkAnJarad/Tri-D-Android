package com.example.testnavdrawer2.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavdrawer2.R;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardData> cardDataList;
    private List<CardData> originalCardDataList;
    private Context context;

    public CardAdapter(Context context, List<CardData> cardDataList) {
        this.context = context;
        this.cardDataList = cardDataList;
        this.originalCardDataList = new ArrayList<>(cardDataList);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CardData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalCardDataList); // Show all if query is empty
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (CardData item : originalCardDataList) {
                        // Filter based on any field (e.g., vehicle type or plate number)
                        if (item.getVehicleType().toLowerCase().contains(filterPattern) ||
                                item.getPlateNumber().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cardDataList.clear();
                cardDataList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    // Optional: helper method to reset the filter (if needed)
    public void resetFilter() {
        cardDataList.clear();
        cardDataList.addAll(originalCardDataList);
        notifyDataSetChanged();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView qrCodeImage, statusIndicator;
        TextView vehicleType, plateNumber, parkingSlot, dateGenerated;

        public CardViewHolder(View itemView) {
            super(itemView);
            qrCodeImage = itemView.findViewById(R.id.qrCodeImage);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
            vehicleType = itemView.findViewById(R.id.vehicleType);
            plateNumber = itemView.findViewById(R.id.plateNumber);
            parkingSlot = itemView.findViewById(R.id.parkingSlot);
            dateGenerated = itemView.findViewById(R.id.dateGenerated);
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardData cardData = cardDataList.get(position);

        // Set data in the views
        holder.vehicleType.setText(cardData.getVehicleType());
        holder.plateNumber.setText(cardData.getPlateNumber());
        holder.parkingSlot.setText(cardData.getParkingSlot());
        holder.dateGenerated.setText(cardData.getDateGenerated());

        // Update QR code image and status indicator
        holder.qrCodeImage.setImageBitmap(cardData.getQrCodeImage());  // Assuming a Bitmap for QR code
        holder.statusIndicator.setImageResource(cardData.isStatus() ? R.drawable.circle_green : R.drawable.circle_red);
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    public void addCard(CardData cardData) {
        cardDataList.add(cardData);
        notifyItemInserted(cardDataList.size() - 1);
    }

    public void removeCard(int position) {
        cardDataList.remove(position);
        notifyItemRemoved(position);
    }


}
