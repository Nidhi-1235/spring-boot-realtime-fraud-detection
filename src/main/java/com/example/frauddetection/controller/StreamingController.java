package com.example.frauddetection.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.frauddetection.config.SocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/stream")
public class StreamingController {

    private final SocketHandler socketHandler;
    private static final String[] LOCATIONS = {"Mumbai", "Bengaluru", "Delhi", "New York", "London"};
    private final Random random = new Random();
    
    private final Map<String, AtomicInteger> locationTrafficCard = new ConcurrentHashMap<>();
    
    // --- USER PROFILE STATE FOR TRACKING GEOGRAPHIC VELOCITY ---
    private final Map<String, String> userLastLocation = new ConcurrentHashMap<>();
    private final Map<String, Long> userLastTimestamp = new ConcurrentHashMap<>();
    
    // --- SIMULATED MERCHANT CONCURRENT TERMINAL STATE ---
    private final Map<String, String> cardActiveMerchantTerminal = new ConcurrentHashMap<>();
    private final Map<String, Long> cardActiveSwipeTimestamp = new ConcurrentHashMap<>();
    
    // --- AUDIT PERSISTENCE LEDGER FOR EXPORT ---
    private final List<String> interceptedAnomalyLedger = new CopyOnWriteArrayList<>();

    private volatile double maxAllowedAmount = 2000.0;
    private volatile double systemSensitivity = 1.0;

    public StreamingController(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
        for (String loc : LOCATIONS) {
            locationTrafficCard.put(loc, new AtomicInteger(0));
        }
    }

    @PostMapping("/update-rules")
    public String updateRules(@RequestParam double maxAmount, @RequestParam double sensitivity) {
        this.maxAllowedAmount = maxAmount;
        this.systemSensitivity = sensitivity;
        return "Rules successfully deployed to memory cache.";
    }

    @GetMapping("/export-logs")
    public ResponseEntity<byte[]> exportLogs() {
        StringBuilder fileContent = new StringBuilder();
        fileContent.append("=====================================================================\n");
        fileContent.append("                SECURE RISK ENGINE: INCIDENT AUDIT LOG               \n");
        fileContent.append("                Generated At: ").append(new Date()).append("\n");
        fileContent.append("=====================================================================\n\n");

        if (interceptedAnomalyLedger.isEmpty()) {
            fileContent.append("No critical anomalies flagged in this tracking window.\n");
        } else {
            for (String log : interceptedAnomalyLedger) {
                fileContent.append(log).append("\n");
            }
        }

        byte[] outputBuffer = fileContent.toString().getBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fraud_anomaly_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(outputBuffer);
    }

    @GetMapping("/start-simulation")
    public String startSimulation() {
        System.out.println("[ENGINE]: Activating Multi-Variable Spatiotemporal Security Engine...");

        Thread simulationThread = new Thread(() -> {
            try {
                for (int i = 0; i < 60; i++) {
                    String userId = "USER-0" + (random.nextInt(5) + 1);
                    String location = LOCATIONS[random.nextInt(LOCATIONS.length)];
                    double amount = 5.0 + (2500.0 * random.nextDouble());
                    long currentTime = System.currentTimeMillis();
                    
                    boolean spatiotemporalViolation = false;
                    boolean merchantCloneViolation = false;
                    double baseRisk = (amount > this.maxAllowedAmount) ? 0.60 : 0.05;

                    // --- HEURISTIC 1: SPATIOTEMPORAL VELOCITY VIOLATION CHECK ---
                    String lastLoc = userLastLocation.get(userId);
                    Long lastTime = userLastTimestamp.get(userId);

                    if (lastLoc != null && lastTime != null && !lastLoc.equals(location)) {
                        long durationSeconds = (currentTime - lastTime) / 1000;
                        if (durationSeconds < 6) {
                            spatiotemporalViolation = true;
                            baseRisk += 0.40; 
                        }
                    }

                    // --- HEURISTIC 2: SIMULATED MERCHANT CARD CONCURRENCY FLAG ---
                    String previousMerchant = cardActiveMerchantTerminal.get(userId);
                    Long previousSwipeTime = cardActiveSwipeTimestamp.get(userId);

                    if (previousMerchant != null && previousSwipeTime != null) {
                        long timeDeltaSeconds = (currentTime - previousSwipeTime) / 1000;
                        
                        // Critical trigger: Different cities with under 4 seconds between swipes
                        if (!previousMerchant.equals(location) && timeDeltaSeconds < 4) {
                            merchantCloneViolation = true;
                            baseRisk = 1.0; // Instantly force risk score to maximum threshold
                        }
                    }

                    // --- HEURISTIC 3: TRAFFIC BURSTS ---
                    int recentTraffic = locationTrafficCard.get(location).incrementAndGet();
                    if (recentTraffic > 3) baseRisk += 0.15;

                    double finalRiskScore = Math.min(0.99, baseRisk * this.systemSensitivity);
                    String status = (finalRiskScore > 0.60) ? "FRAUD" : "APPROVED";

                    // Update standard tracking states
                    userLastLocation.put(userId, location);
                    userLastTimestamp.put(userId, currentTime);

                    // Update merchant concurrent tracking states
                    cardActiveMerchantTerminal.put(userId, location);
                    cardActiveSwipeTimestamp.put(userId, currentTime);

                    if (random.nextBoolean()) {
                        locationTrafficCard.get(location).updateAndGet(v -> Math.max(0, v - 1));
                    }

                    String txId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    
                    // Unified JSON payload broadcasting flags down to the UI layer
                    String jsonPayload = String.format(
                        "{\"id\":\"%s\",\"user\":\"%s\",\"location\":\"%s\",\"amount\":%.2f,\"riskScore\":%.2f,\"status\":\"%s\",\"velocityFlag\":%b,\"merchantCloneFlag\":%b}",
                        txId, userId, location, amount, finalRiskScore, status, spatiotemporalViolation, merchantCloneViolation
                    );

                    // Compile audit logging details
                    if ("FRAUD".equals(status)) {
                        String incidentType = "[POLICY REASONING]: ";
                        if (merchantCloneViolation) incidentType += "CRITICAL CARD CLONE CONCURRENCY THREAT (SIMULTANEOUS SWIPES)";
                        else if (spatiotemporalViolation) incidentType += "SPATIOTEMPORAL GEO-SPEED VIOLATION";
                        else incidentType += "LIMIT RULE EXCEEDED";

                        String logEntry = String.format("[%s] - ID: %s | Account: %s | Origin: %s | Amount: $%.2f | Risk: %.0f%% | %s",
                                new Date(), txId, userId, location, amount, (finalRiskScore * 100), incidentType);
                        interceptedAnomalyLedger.add(logEntry);
                    }

                    socketHandler.broadcastMessage(jsonPayload);
                    Thread.sleep(1200);
                }
            } catch (InterruptedException e) {
                System.err.println("Simulation interrupted: " + e.getMessage());
            }
        });

        simulationThread.start();
        return "Intelligent fraud simulation engine spinning up.";
    }
}