package com.example.testnavdrawer2;

public class VehicleEntry {
    private String plateNumber;
    private String type;
    private String brand;
    private String color;
    private boolean verified;

    public VehicleEntry(String plateNumber, String type, String brand, String color, boolean verified) {
        this.plateNumber = plateNumber;
        this.type = type;
        this.brand = brand;
        this.color = color;
        this.verified = verified;
    }

    // Getters
    public String getPlateNumber() { return plateNumber; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public String getColor() { return color; }
    public boolean isVerified() { return verified; }
}
