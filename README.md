# 🛡️ Real-Time Fraud Detection Engine

A high-performance, low-latency Spring Boot fraud detection application engineered to analyze incoming transaction streams and flag high-risk anomalies in real time. Features a live visualization dashboard powered by WebSockets and Chart.js, alongside zero-downtime **Dynamic Policy Hot-Reloading**.

---

## 🚀 Key Features

- ⚡ **Sub-Millisecond Fraud Evaluation:** In-memory sliding-window analysis for velocity-based transaction checks and spatiotemporal anomaly detection.
- 🔄 **Dynamic Policy Hot-Reloading:** Modify administrative fraud thresholds dynamically via REST APIs without restarting the server or interrupting active WebSocket connections.
- 📊 **Live Dashboard:** Real-time metrics streamed through WebSockets and visualized with Chart.js.
- 🛡️ **Thread-Safe State Management:** Concurrent rule engine configuration for high-throughput transaction processing.
- 🌍 **Real-Time Alert Broadcasting:** Fraud events are instantly pushed to connected dashboard clients.
- 🚀 **Zero-Downtime Configuration Updates:** Fraud policies are applied immediately across all active evaluation threads.

---

## 🏗️ System Architecture

```text
                    Incoming Transaction Stream
                               │
                               ▼
                  ┌────────────────────────┐
                  │  Fraud Engine Service  │
                  │ Sliding Window Engine  │
                  └──────────┬─────────────┘
                             │
             ┌───────────────┴───────────────┐
             ▼                               ▼
     Rule Evaluation                 REST Admin API
             │                  (Dynamic Rule Updates)
             └───────────────┬───────────────┘
                             ▼
                   WebSocket Notification
                             │
                             ▼
                 Live Dashboard (Chart.js)
```

---

# ⚙️ Dynamic Policy Hot-Reloading API

The fraud detection engine supports **runtime policy updates** without requiring a server restart. Administrative fraud thresholds are immediately propagated across all active evaluation threads while maintaining uninterrupted WebSocket connections.

## ✨ Features

- ✅ No server restart required
- ✅ Zero downtime
- ✅ Instant propagation across active threads
- ✅ Thread-safe configuration updates
- ✅ Existing WebSocket connections remain active

---

## 📌 Update Fraud Rules

### Endpoint

```http
POST /api/rules/update
Content-Type: application/json
```

### Request Body

```json
{
  "maxTransactionAmount": 10000.00,
  "velocityTimeWindowSeconds": 60,
  "maxVelocityCount": 5
}
```

### Success Response

```json
{
  "status": "SUCCESS",
  "message": "Policy hot-reloaded successfully across active evaluation threads."
}
```

---

## 📖 Rule Description

| Field | Description |
|-------|-------------|
| `maxTransactionAmount` | Maximum amount allowed before a transaction is flagged. |
| `velocityTimeWindowSeconds` | Time window used for velocity-based fraud detection. |
| `maxVelocityCount` | Maximum transactions allowed within the configured time window. |

---

## 🔄 Hot Reload Workflow

```text
Admin
  │
  ▼
POST /api/rules/update
  │
  ▼
REST Controller
  │
  ▼
Thread-Safe Rule Manager
  │
  ▼
Update Active Fraud Engine
  │
  ▼
Immediate Rule Enforcement
  │
  ▼
Live Dashboard Updates
```

---

## 🛠️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Spring Boot 3.x
- Git
- IntelliJ IDEA / Eclipse / VS Code

---

### Clone the Repository

```bash
git clone https://github.com/Nidhi-1235/spring-boot-realtime-fraud-detection.git

cd spring-boot-realtime-fraud-detection
```

---

### Build the Project

```bash
mvn clean install
```

---

### Run the Application

```bash
mvn spring-boot:run
```

---

### Access the Live Dashboard

Open your browser and navigate to:

```text
http://localhost:8086/dashboard.html
```

---

## 📡 Available REST APIs

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/transactions` | Submit a new transaction for fraud evaluation |
| POST | `/api/rules/update` | Dynamically update fraud detection rules |
| GET | `/dashboard.html` | Open the live monitoring dashboard |

---

## 🧰 Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring WebSocket
- Maven
- Chart.js
- HTML5
- CSS3
- JavaScript
- ConcurrentHashMap
- AtomicReference

---

## 🎯 Highlights

- ⚡ Sub-millisecond fraud detection
- 🔄 Dynamic policy hot-reloading
- 📊 Real-time Chart.js dashboard
- 📡 WebSocket live event streaming
- 🛡️ Thread-safe concurrent processing
- 🚀 Zero-downtime rule updates
- 📈 Sliding-window velocity detection
- 🌍 Production-ready architecture

  ## 👩‍💻 Author

**Nidhi N**
---
