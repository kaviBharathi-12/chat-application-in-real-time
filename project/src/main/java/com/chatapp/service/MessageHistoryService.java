package com.chatapp.service;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SINGLETON PATTERN (Creational)
 * Manages message history for chat rooms
 * Provides persistent message storage and retrieval
 */
public class MessageHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(MessageHistoryService.class);
    private static volatile MessageHistoryService instance;
    private static final Object lock = new Object();
    
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Message>> messageHistory;
    private static final int MAX_MESSAGES_PER_ROOM = 1000;
    
    private MessageHistoryService() {
        this.messageHistory = new ConcurrentHashMap<>();
        logger.info("Message History Service initialized");
    }
    
    public static MessageHistoryService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new MessageHistoryService();
                }
            }
        }
        return instance;
    }
    
    public void saveMessage(String roomId, Message message) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        CopyOnWriteArrayList<Message> roomMessages = messageHistory.computeIfAbsent(
            roomId, k -> new CopyOnWriteArrayList<>());
        
        roomMessages.add(message);
        
        // Maintain maximum message limit per room
        if (roomMessages.size() > MAX_MESSAGES_PER_ROOM) {
            roomMessages.remove(0); // Remove oldest message
        }
        
        logger.debug("Message saved for room " + roomId + ": " + message.getMessageId());
    }
    
    public List<Message> getMessageHistory(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        CopyOnWriteArrayList<Message> roomMessages = messageHistory.get(roomId);
        if (roomMessages == null) {
            return new ArrayList<>();
        }
        
        // Return messages in reverse chronological order (newest first)
        List<Message> messages = new ArrayList<>(roomMessages);
        Collections.reverse(messages);
        
        return messages;
    }
    
    public List<Message> getRecentMessages(String roomId, int count) {
        List<Message> allMessages = getMessageHistory(roomId);
        
        if (count <= 0 || allMessages.isEmpty()) {
            return new ArrayList<>();
        }
        
        return allMessages.subList(0, Math.min(count, allMessages.size()));
    }
    
    public int getMessageCount(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return 0;
        }
        
        CopyOnWriteArrayList<Message> roomMessages = messageHistory.get(roomId);
        return roomMessages != null ? roomMessages.size() : 0;
    }
    
    public void clearHistory(String roomId) {
        if (roomId != null && !roomId.trim().isEmpty()) {
            CopyOnWriteArrayList<Message> removed = messageHistory.remove(roomId);
            if (removed != null) {
                logger.info("Message history cleared for room: " + roomId);
            }
        }
    }
    
    public int getTotalMessages() {
        return messageHistory.values().stream()
                .mapToInt(List::size)
                .sum();
    }
    
    public int getTotalRoomsWithHistory() {
        return messageHistory.size();
    }
}