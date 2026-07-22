package com.example.frauddetection.service;
import com.example.frauddetection.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class FraudModelService {

    /**
     * Generates a fraud probability score between 0.0 and 1.0 based on engineered features
     */
    public double evaluateTransaction(Transaction tx) {
        double fraudScore = 0.0;

        // Condition 1: High velocity (Classic botnet/stolen card behavior)
        if (tx.getTransactionCountLast2Min() > 5) {
            fraudScore += 0.4;
        }

        // Condition 2: Sudden drastic spending spike
        if (tx.getAmount() > (tx.getAverageAmountLast24Hours() * 4)) {
            fraudScore += 0.35;
        }

        // Condition 3: Location impossible velocity (e.g., London then Mumbai in 5 minutes)
        if (tx.isLocationAnomaly()) {
            fraudScore += 0.25;
        }

        // Normalize score cap
        return Math.min(fraudScore, 1.0);
    }
}