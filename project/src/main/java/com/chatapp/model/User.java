package com.chatapp.model;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a user in the chat system
 * Maintains user state and chat room associations
 */
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    
    private final String username;
    private final String userId;
    private final LocalDateTime joinedAt;
    private boolean isOnline;
    private final CopyOnWriteArrayList<ChatRoom> joinedRooms;
    
    public User(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        this.username = username.trim();
        this.userId = generateUserId(username);
        this.joinedAt = LocalDateTime.now();
        this.isOnline = true;
        this.joinedRooms = new CopyOnWriteArrayList<>();
        
        logger.info("User created: " + username);
    }
    
    private String generateUserId(String username) {
        return username.toLowerCase().replaceAll("[^a-z0-9]", "") + 
               "_" + System.currentTimeMillis() % 10000;
    }
    
    public void addObserver(ChatRoom room) {
        if (room != null && !joinedRooms.contains(room)) {
            joinedRooms.add(room);
            room.addObserver(new UserChatObserver(this));
        }
    }
    
    public void removeObserver(ChatRoom room) {
        if (room != null) {
            joinedRooms.remove(room);
        }
    }
    
    public void setOnline(boolean online) {
        this.isOnline = online;
        logger.info("User " + username + " status changed to: " + (online ? "online" : "offline"));
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    public int getJoinedRoomsCount() {
        return joinedRooms.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userId, user.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    @Override
    public String toString() {
        return String.format("User[%s, ID: %s, Online: %s]", 
                           username, userId, isOnline);
    }
    
    /**
     * Inner class implementing ChatObserver for this user
     */
    private static class UserChatObserver implements com.chatapp.observer.ChatObserver {
        private final User user;
        
        public UserChatObserver(User user) {
            this.user = user;
        }
        
        @Override
        public void onMessageReceived(ChatRoom room, Message message) {
            // User receives notification of new message
            // In a real application, this would update the UI or send notifications
        }
        
        @Override
        public void onPrivateMessageReceived(ChatRoom room, Message message) {
            // User receives notification of private message
            if (message.getRecipient().equals(user)) {
                // This user is the recipient of the private message
            }
        }
        
        @Override
        public void onUserJoined(ChatRoom room, User joinedUser) {
            // User receives notification of someone joining
        }
        
        @Override
        public void onUserLeft(ChatRoom room, User leftUser) {
            // User receives notification of someone leaving
        }
    }
}