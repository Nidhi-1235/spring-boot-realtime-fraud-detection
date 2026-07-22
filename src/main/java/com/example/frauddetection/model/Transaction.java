package com.example.frauddetection.model;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String userId;
    private double amount;
    private String location;
    private LocalDateTime timestamp;
    
    // Feature Engineering metrics
    private int transactionCountLast2Min;
    private double averageAmountLast24Hours;
    private boolean locationAnomaly;

    // Default Constructor
    public Transaction() {}

    // Parameterized Constructor
    public Transaction(String transactionId, String userId, double amount, String location, 
                       LocalDateTime timestamp, int transactionCountLast2Min, 
                       double averageAmountLast24Hours, boolean locationAnomaly) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.location = location;
        this.timestamp = timestamp;
        this.transactionCountLast2Min = transactionCountLast2Min;
        this.averageAmountLast24Hours = averageAmountLast24Hours;
        this.locationAnomaly = locationAnomaly;
    }

    // --- MANUAL GETTERS AND SETTERS (No Lombok required) ---

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getTransactionCountLast2Min() { return transactionCountLast2Min; }
    public void setTransactionCountLast2Min(int transactionCountLast2Min) { this.transactionCountLast2Min = transactionCountLast2Min; }

    public double getAverageAmountLast24Hours() { return averageAmountLast24Hours; }
    public void setAverageAmountLast24Hours(double averageAmountLast24Hours) { this.averageAmountLast24Hours = averageAmountLast24Hours; }

    public boolean isLocationAnomaly() { return locationAnomaly; }
    public void setLocationAnomaly(boolean locationAnomaly) { this.locationAnomaly = locationAnomaly; }
}