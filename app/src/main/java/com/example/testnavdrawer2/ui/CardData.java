package com.example.testnavdrawer2.ui;

import android.graphics.Bitmap;

public class CardData {
    private String vehicleType;
    private String plateNumber;
    private String parkingSlot;
    private String dateGenerated;
    private Bitmap qrCodeImage;
    private boolean status;

    // Constructor, getters, and setters
    public CardData(String vehicleType, String plateNumber, String parkingSlot,
                    String dateGenerated, Bitmap qrCodeImage, boolean status) {
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.parkingSlot = parkingSlot;
        this.dateGenerated = dateGenerated;
        this.qrCodeImage = qrCodeImage;
        this.status = status;
    }

    public String getVehicleType() { return vehicleType; }
    public String getPlateNumber() { return plateNumber; }
    public String getParkingSlot() { return parkingSlot; }
    public String getDateGenerated() { return dateGenerated; }
    public Bitmap getQrCodeImage() { return qrCodeImage; }
    public boolean isStatus() { return status; }
}

