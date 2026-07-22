package com.example.frauddetection.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    // Thread-safe list holding all open browser dashboard connections
    private final List<WebSocketSession> activeSessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.add(session);
        System.out.println("[SOCKET]: Handshake Successful! Live session registered. Active count: " + activeSessions.size());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Echo or handle inbound frame packets from client if necessary
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        activeSessions.remove(session);
        System.out.println("[SOCKET]: Session closed cleanly. Remaining target count: " + activeSessions.size());
    }

    public void broadcastMessage(String payload) {
        System.out.println("[SOCKET]: Broadcasting payload tracking frame to active sessions...");
        
        for (WebSocketSession session : activeSessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(payload));
                } catch (IOException e) {
                    System.err.println("[SOCKET ERROR]: Failed to dispatch frame to session " + session.getId() + " -> " + e.getMessage());
                }
            } else {
                activeSessions.remove(session);
            }
        }
    }
}