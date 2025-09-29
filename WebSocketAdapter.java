package com.chatapp.adapter;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.Message;
import com.chatapp.model.User;

/**
 * ADAPTER PATTERN (Structural) - Concrete Adapter
 * Simulates WebSocket communication adapter
 * Demonstrates how different protocols can be integrated seamlessly
 */
public class WebSocketAdapter implements CommunicationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketAdapter.class);
    
    private boolean connected;
    private String serverEndpoint;
    
    public WebSocketAdapter(String serverEndpoint) {
        this.serverEndpoint = serverEndpoint != null ? serverEndpoint : "ws://localhost:8080/chat";
        this.connected = false;
        logger.info("WebSocket Communication Adapter initialized for: " + this.serverEndpoint);
    }
    
    @Override
    public void sendMessage(Message message) {
        if (!connected) {
            logger.warn("WebSocket not connected, cannot send message");
            return;
        }
        
        try {
            // Simulate WebSocket message sending
            String jsonMessage = formatMessageAsJson(message);
            simulateWebSocketSend(jsonMessage);
            
            logger.debug("Message sent via WebSocket: " + message.getMessageId());
            
        } catch (Exception e) {
            logger.error("Error sending message via WebSocket", e);
        }
    }
    
    @Override
    public void notifyUserJoined(User user, String roomId) {
        if (!connected) {
            return;
        }
        
        try {
            String notification = String.format(
                "{\"type\":\"user_joined\",\"user\":\"%s\",\"room\":\"%s\",\"timestamp\":\"%d\"}",
                user.getUsername(), roomId, System.currentTimeMillis());
            
            simulateWebSocketSend(notification);
            logger.debug("User join notification sent via WebSocket: " + user.getUsername());
            
        } catch (Exception e) {
            logger.error("Error sending user join notification via WebSocket", e);
        }
    }
    
    @Override
    public void notifyUserLeft(User user, String roomId) {
        if (!connected) {
            return;
        }
        
        try {
            String notification = String.format(
                "{\"type\":\"user_left\",\"user\":\"%s\",\"room\":\"%s\",\"timestamp\":\"%d\"}",
                user.getUsername(), roomId, System.currentTimeMillis());
            
            simulateWebSocketSend(notification);
            logger.debug("User leave notification sent via WebSocket: " + user.getUsername());
            
        } catch (Exception e) {
            logger.error("Error sending user leave notification via WebSocket", e);
        }
    }
    
    @Override
    public void displaySystemMessage(String message) {
        if (!connected) {
            return;
        }
        
        try {
            String systemMessage = String.format(
                "{\"type\":\"system_message\",\"content\":\"%s\",\"timestamp\":\"%d\"}",
                message, System.currentTimeMillis());
            
            simulateWebSocketSend(systemMessage);
            logger.debug("System message sent via WebSocket: " + message);
            
        } catch (Exception e) {
            logger.error("Error sending system message via WebSocket", e);
        }
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public void connect() {
        try {
            // Simulate WebSocket connection
            simulateConnection();
            connected = true;
            logger.info("WebSocket connected to: " + serverEndpoint);
            
        } catch (Exception e) {
            logger.error("Failed to connect WebSocket", e);
            connected = false;
        }
    }
    
    @Override
    public void disconnect() {
        if (connected) {
            try {
                // Simulate WebSocket disconnection
                simulateDisconnection();
                connected = false;
                logger.info("WebSocket disconnected from: " + serverEndpoint);
                
            } catch (Exception e) {
                logger.error("Error during WebSocket disconnection", e);
            }
        }
    }
    
    @Override
    public String getProtocolType() {
        return "WEBSOCKET";
    }
    
    private String formatMessageAsJson(Message message) {
        return String.format(
            "{\"type\":\"%s\",\"messageId\":\"%s\",\"sender\":\"%s\",\"content\":\"%s\",\"timestamp\":\"%s\"%s}",
            message.isPrivate() ? "private_message" : "public_message",
            message.getMessageId(),
            message.getSender().getUsername(),
            escapeJson(message.getContent()),
            message.getTimestamp().toString(),
            message.isPrivate() ? ",\"recipient\":\"" + message.getRecipient().getUsername() + "\"" : ""
        );
    }
    
    private String escapeJson(String content) {
        return content.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    private void simulateWebSocketSend(String data) {
        // Simulate network delay
        try {
            Thread.sleep(10); // 10ms simulated network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // In a real implementation, this would send data over WebSocket
        logger.debug("WebSocket data sent: " + data.substring(0, Math.min(100, data.length())));
    }
    
    private void simulateConnection() throws Exception {
        // Simulate connection establishment
        Thread.sleep(100); // Simulate connection time
        
        // In a real implementation, this would establish WebSocket connection
        if (Math.random() < 0.95) { // 95% success rate simulation
            logger.debug("WebSocket connection established successfully");
        } else {
            throw new Exception("Connection failed - server unavailable");
        }
    }
    
    private void simulateDisconnection() {
        // Simulate graceful disconnection
        try {
            Thread.sleep(50); // Simulate disconnection time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.debug("WebSocket connection closed gracefully");
    }
    
    public String getServerEndpoint() {
        return serverEndpoint;
    }
    
    public void setServerEndpoint(String serverEndpoint) {
        if (!connected) {
            this.serverEndpoint = serverEndpoint;
            logger.info("WebSocket endpoint updated to: " + serverEndpoint);
        } else {
            logger.warn("Cannot change endpoint while connected");
        }
    }
}