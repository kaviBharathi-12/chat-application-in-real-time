package com.chatapp.model;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.observer.ChatObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * OBSERVER PATTERN (Behavioral) - Subject
 * Represents a chat room that notifies observers of new messages and user activities
 * Demonstrates real-time notification system
 */
public class ChatRoom {
    private static final Logger logger = LoggerFactory.getLogger(ChatRoom.class);
    
    private final String roomId;
    private final User admin;
    private final LocalDateTime createdAt;
    private final List<User> activeUsers;
    private final List<ChatObserver> observers;
    
    public ChatRoom(String roomId, User admin) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (admin == null) {
            throw new IllegalArgumentException("Admin cannot be null");
        }
        
        this.roomId = roomId;
        this.admin = admin;
        this.createdAt = LocalDateTime.now();
        this.activeUsers = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
        
        // Admin automatically joins the room
        addUser(admin);
        
        logger.info("Chat room created: " + roomId);
    }
    
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (!activeUsers.contains(user)) {
            activeUsers.add(user);
            user.addObserver(this);
            notifyUserJoined(user);
            logger.info("User joined room " + roomId + ": " + user.getUsername());
        }
    }
    
    public void removeUser(User user) {
        if (user == null) {
            return;
        }
        
        if (activeUsers.remove(user)) {
            user.removeObserver(this);
            notifyUserLeft(user);
            logger.info("User left room " + roomId + ": " + user.getUsername());
        }
    }
    
    public void broadcastMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        if (!activeUsers.contains(message.getSender())) {
            throw new IllegalArgumentException("Sender is not in this room");
        }
        
        notifyNewMessage(message);
        logger.info("Message broadcast in room " + roomId + " by " + message.getSender().getUsername());
    }
    
    public void sendPrivateMessage(Message privateMessage) {
        if (privateMessage == null || !privateMessage.isPrivate()) {
            throw new IllegalArgumentException("Invalid private message");
        }
        
        if (!activeUsers.contains(privateMessage.getSender()) || 
            !activeUsers.contains(privateMessage.getRecipient())) {
            throw new IllegalArgumentException("Both sender and recipient must be in this room");
        }
        
        notifyPrivateMessage(privateMessage);
        logger.info("Private message sent in room " + roomId + " from " + 
                   privateMessage.getSender().getUsername() + " to " + 
                   privateMessage.getRecipient().getUsername());
    }
    
    // Observer pattern methods
    public void addObserver(ChatObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.debug("Observer added to room: " + roomId);
        }
    }
    
    public void removeObserver(ChatObserver observer) {
        if (observer != null) {
            observers.remove(observer);
            logger.debug("Observer removed from room: " + roomId);
        }
    }
    
    private void notifyNewMessage(Message message) {
        for (ChatObserver observer : observers) {
            try {
                observer.onMessageReceived(this, message);
            } catch (Exception e) {
                logger.error("Error notifying observer of new message", e);
            }
        }
    }
    
    private void notifyPrivateMessage(Message message) {
        for (ChatObserver observer : observers) {
            try {
                observer.onPrivateMessageReceived(this, message);
            } catch (Exception e) {
                logger.error("Error notifying observer of private message", e);
            }
        }
    }
    
    private void notifyUserJoined(User user) {
        for (ChatObserver observer : observers) {
            try {
                observer.onUserJoined(this, user);
            } catch (Exception e) {
                logger.error("Error notifying observer of user join", e);
            }
        }
    }
    
    private void notifyUserLeft(User user) {
        for (ChatObserver observer : observers) {
            try {
                observer.onUserLeft(this, user);
            } catch (Exception e) {
                logger.error("Error notifying observer of user leave", e);
            }
        }
    }
    
    // Getters
    public String getRoomId() {
        return roomId;
    }
    
    public User getAdmin() {
        return admin;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public List<User> getActiveUsers() {
        return new ArrayList<>(activeUsers);
    }
    
    public int getUserCount() {
        return activeUsers.size();
    }
    
    public boolean isUserInRoom(User user) {
        return activeUsers.contains(user);
    }
    
    @Override
    public String toString() {
        return String.format("ChatRoom[ID: %s, Users: %d, Admin: %s]", 
                           roomId, activeUsers.size(), admin.getUsername());
    }
}