package com.example.frauddetection.service;
import com.example.frauddetection.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class FeatureEngine {

    private final Map<String, List<Transaction>> userHistoryMap = new ConcurrentHashMap<>();

    public Transaction enrichFeatures(Transaction currentTx) {
        String userId = currentTx.getUserId();
        
        userHistoryMap.putIfAbsent(userId, new CopyOnWriteArrayList<>());
        List<Transaction> history = userHistoryMap.get(userId);

        LocalDateTime now = currentTx.getTimestamp();

        // Fix: Use Java 8 removeIf BEFORE processing to cleanly purge data older than 24 hours
        history.removeIf(oldTx -> oldTx.getTimestamp().isBefore(now.minusHours(24)));

        // Now calculate metrics cleanly without modifying the list during iteration
        int countLast2Min = 0;
        double totalAmount24Hours = 0;
        boolean locationAnomaly = false;

        for (Transaction oldTx : history) {
            // Metric A: Velocity check
            if (oldTx.getTimestamp().isAfter(now.minusMinutes(2))) {
                countLast2Min++;
            }

            // Metric B: Accumulate for average
            totalAmount24Hours += oldTx.getAmount();

            // Metric C: Quick location check
            if (!oldTx.getLocation().equals(currentTx.getLocation()) && 
                oldTx.getTimestamp().isAfter(now.minusMinutes(10))) {
                locationAnomaly = true;
            }
        }

        // Set the calculated features
        currentTx.setTransactionCountLast2Min(countLast2Min + 1); // include current transaction
        
        double avg = history.isEmpty() ? currentTx.getAmount() : (totalAmount24Hours / history.size());
        currentTx.setAverageAmountLast24Hours(avg);
        currentTx.setLocationAnomaly(locationAnomaly);

        // Commit current transaction to historical list
        history.add(currentTx);

        return currentTx;
    }
}