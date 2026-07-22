# 🛡️ Real-Time Fraud Detection Engine

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
