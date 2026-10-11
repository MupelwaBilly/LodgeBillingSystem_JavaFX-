package com.example.lodgebilling.model;

public class Bill {
    private String billId;
    private String guestName;
    private String roomType;
    private int nights;
    private double extras;
    private double amountPaid;
    private double total;
    private double balance;

    public Bill(String billId, String guestName, String roomType, int nights, double ratePerNight, double extras, double amountPaid) {
        this.billId = billId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.extras = extras;
        this.amountPaid = amountPaid;

        // Business Logic: 5 nights or more gets 5% off the room charge. Extras get no discount.
        double roomCharge = nights * ratePerNight;
        double discount = (nights >= 5) ? roomCharge * 0.05 : 0.0;

        this.total = (roomCharge - discount) + extras;
        this.balance = this.total - amountPaid;
    }

    // Getters required for JavaFX TableView PropertyValueFactory
    public String getBillId() { return billId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public double getExtras() { return extras; }
    public double getAmountPaid() { return amountPaid; }
    public double getTotal() { return total; }
    public double getBalance() { return balance; }

    // Formatted getters for the table columns
    public String getFormattedTotal() { return String.format("K %.2f", total); }
    public String getFormattedPaid() { return String.format("K %.2f", amountPaid); }
    public String getFormattedBalance() { return String.format("K %.2f", balance); }
}