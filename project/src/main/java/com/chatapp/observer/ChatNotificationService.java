package com.chatapp.observer;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.ChatRoom;
import com.chatapp.model.Message;
import com.chatapp.model.User;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OBSERVER PATTERN (Behavioral) - Concrete Observer
 * Handles real-time notifications for chat events
 * Demonstrates how observers react to chat room activities
 */
public class ChatNotificationService implements ChatObserver {
    private static final Logger logger = LoggerFactory.getLogger(ChatNotificationService.class);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private final AtomicInteger messageCount;
    private final AtomicInteger userActivityCount;
    
    public ChatNotificationService() {
        this.messageCount = new AtomicInteger(0);
        this.userActivityCount = new AtomicInteger(0);
        logger.info("Chat Notification Service initialized");
    }
    
    @Override
    public void onMessageReceived(ChatRoom room, Message message) {
        try {
            messageCount.incrementAndGet();
            
            String timestamp = message.getTimestamp().format(TIME_FORMAT);
            String notification = String.format("🔔 [%s] New message in %s from %s: %s",
                timestamp, room.getRoomId(), message.getSender().getUsername(), 
                truncateMessage(message.getContent()));
            
            System.out.println(notification);
            logger.info("Message notification sent for room: " + room.getRoomId());
            
        } catch (Exception e) {
            logger.error("Error processing message notification", e);
        }
    }
    
    @Override
    public void onPrivateMessageReceived(ChatRoom room, Message message) {
        try {
            messageCount.incrementAndGet();
            
            String timestamp = message.getTimestamp().format(TIME_FORMAT);
            String notification = String.format("💌 [%s] Private message in %s from %s to %s",
                timestamp, room.getRoomId(), 
                message.getSender().getUsername(),
                message.getRecipient().getUsername());
            
            System.out.println(notification);
            logger.info("Private message notification sent for room: " + room.getRoomId());
            
        } catch (Exception e) {
            logger.error("Error processing private message notification", e);
        }
    }
    
    @Override
    public void onUserJoined(ChatRoom room, User user) {
        try {
            userActivityCount.incrementAndGet();
            
            String notification = String.format("👋 %s joined room %s (Total users: %d)",
                user.getUsername(), room.getRoomId(), room.getUserCount());
            
            System.out.println(notification);
            logger.info("User join notification sent: " + user.getUsername() + " -> " + room.getRoomId());
            
        } catch (Exception e) {
            logger.error("Error processing user join notification", e);
        }
    }
    
    @Override
    public void onUserLeft(ChatRoom room, User user) {
        try {
            userActivityCount.incrementAndGet();
            
            String notification = String.format("👋 %s left room %s (Remaining users: %d)",
                user.getUsername(), room.getRoomId(), room.getUserCount());
            
            System.out.println(notification);
            logger.info("User leave notification sent: " + user.getUsername() + " <- " + room.getRoomId());
            
        } catch (Exception e) {
            logger.error("Error processing user leave notification", e);
        }
    }
    
    private String truncateMessage(String content) {
        if (content.length() > 50) {
            return content.substring(0, 47) + "...";
        }
        return content;
    }
    
    public void printStatistics() {
        System.out.println("\n📊 Notification Statistics:");
        System.out.println("Messages processed: " + messageCount.get());
        System.out.println("User activities: " + userActivityCount.get());
        System.out.println("Total notifications: " + (messageCount.get() + userActivityCount.get()));
    }
    
    public int getTotalNotifications() {
        return messageCount.get() + userActivityCount.get();
    }
    
    public void reset() {
        messageCount.set(0);
        userActivityCount.set(0);
        logger.info("Notification statistics reset");
    }
}