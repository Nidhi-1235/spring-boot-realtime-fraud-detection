# 🛡️ Real-Time Fraud Detection Engine

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

A high-performance, low-latency Spring Boot fraud detection application engineered to analyze incoming transaction streams and flag high-risk anomalies in real time. Features a live visualization dashboard powered by WebSockets and Chart.js, alongside zero-downtime **Dynamic Policy Hot-Reloading**.

---

## 🚀 Key Features

* **Sub-Millisecond Fraud Evaluation:** In-memory sliding-window analysis for velocity-based transaction checks and spatiotemporal anomaly detection.
* **⚡ Dynamic Policy Hot-Reloading:** Modify administrative fraud thresholds dynamically via REST endpoints without requiring server restarts or causing WebSocket disconnections.
* **📊 Live Dashboard:** Real-time metrics streaming over WebSockets to Chart.js, providing visual velocity tracking and immediate alert notifications.
* **🛡️ Thread-Safe State Management:** Concurrent rule engine configuration designed for multi-threaded high-throughput processing.

---

## 🏗️ System Architecture

```text
[ Incoming Transaction Stream ]
               │
               ▼
   [ Fraud Engine Service ] ──(In-Memory Sliding Windows)
               │
   ┌───────────┴───────────┐
   ▼                       ▼
[ Rule Evaluation ]    [ REST Admin API ] ──(Dynamic Threshold Updates)
   │                       │
   └───────────┬───────────┘
               ▼
     [ WebSocket Channel ]
               │
               ▼
    [ Live Chart.js UI ]

⚙️ Dynamic Policy Hot-Reloading API
Administrative limits (such as single-transaction ceilings or velocity flags) can be updated dynamically at runtime without server restarts or connection drops.

Update Fraud Rules
HTTP
POST /api/rules/update
Content-Type: application/json

{
  "maxTransactionAmount": 10000.00,
  "velocityTimeWindowSeconds": 60,
  "maxVelocityCount": 5
}
Response:

JSON
{
  "status": "SUCCESS",
  "message": "Policy hot-reloaded successfully across active evaluation threads."
}
🛠️ Getting Started
Prerequisites
Java 17+

Maven 3.8+

An IDE (Eclipse, IntelliJ IDEA, or VS Code)

Running Locally
Clone the repository:

Bash
git clone [https://github.com/Nidhi-1235/spring-boot-realtime-fraud-detection.git](https://github.com/Nidhi-1235/spring-boot-realtime-fraud-detection.git)
cd spring-boot-realtime-fraud-detection
Build and package:

Bash
mvn clean install
Run the Spring Boot application:

Bash
mvn spring-boot:run
Access the Live Dashboard:
Open your browser and navigate to:

Plaintext
http://localhost:8086/dashboard.html
