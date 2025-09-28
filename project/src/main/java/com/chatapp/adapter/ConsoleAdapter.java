package com.chatapp.adapter;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.Message;
import com.chatapp.model.User;

import java.time.format.DateTimeFormatter;

/**
 * ADAPTER PATTERN (Structural) - Concrete Adapter
 * Adapts console-based communication to the standard communication interface
 * Demonstrates how different communication protocols can be integrated
 */
public class ConsoleAdapter implements CommunicationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleAdapter.class);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private boolean connected;
    
    public ConsoleAdapter() {
        this.connected = false;
        logger.info("Console Communication Adapter initialized");
    }
    
    @Override
    public void sendMessage(Message message) {
        if (!connected) {
            logger.warn("Attempted to send message while disconnected");
            return;
        }
        
        try {
            String timestamp = message.getTimestamp().format(TIME_FORMAT);
            
            if (message.isPrivate()) {
                System.out.printf("💌 [%s] PRIVATE %s -> %s: %s%n",
                    timestamp,
                    message.getSender().getUsername(),
                    message.getRecipient().getUsername(),
                    message.getContent());
            } else {
                System.out.printf("💬 [%s] %s: %s%n",
                    timestamp,
                    message.getSender().getUsername(),
                    message.getContent());
            }
            
            logger.debug("Message sent via console adapter: " + message.getMessageId());
            
        } catch (Exception e) {
            logger.error("Error sending message via console adapter", e);
        }
    }
    
    @Override
    public void notifyUserJoined(User user, String roomId) {
        if (!connected) {
            return;
        }
        
        try {
            System.out.println("🟢 " + user.getUsername() + " joined room " + roomId);
            logger.debug("User join notification sent via console: " + user.getUsername());
        } catch (Exception e) {
            logger.error("Error sending user join notification", e);
        }
    }
    
    @Override
    public void notifyUserLeft(User user, String roomId) {
        if (!connected) {
            return;
        }
        
        try {
            System.out.println("🔴 " + user.getUsername() + " left room " + roomId);
            logger.debug("User leave notification sent via console: " + user.getUsername());
        } catch (Exception e) {
            logger.error("Error sending user leave notification", e);
        }
    }
    
    @Override
    public void displaySystemMessage(String message) {
        if (!connected) {
            return;
        }
        
        try {
            System.out.println("🔔 SYSTEM: " + message);
            logger.debug("System message displayed: " + message);
        } catch (Exception e) {
            logger.error("Error displaying system message", e);
        }
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public void connect() {
        if (!connected) {
            connected = true;
            System.out.println("🔗 Console communication established");
            logger.info("Console adapter connected");
        }
    }
    
    @Override
    public void disconnect() {
        if (connected) {
            connected = false;
            System.out.println("🔌 Console communication disconnected");
            logger.info("Console adapter disconnected");
        }
    }
    
    @Override
    public String getProtocolType() {
        return "CONSOLE";
    }
}