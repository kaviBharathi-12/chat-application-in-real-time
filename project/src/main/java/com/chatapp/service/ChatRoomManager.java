package com.chatapp.service;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.ChatRoom;
import com.chatapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SINGLETON PATTERN (Creational)
 * Manages all chat rooms in the system
 * Ensures centralized room management with thread safety
 */
public class ChatRoomManager {
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomManager.class);
    private static volatile ChatRoomManager instance;
    private static final Object lock = new Object();
    
    private final ConcurrentHashMap<String, ChatRoom> chatRooms;
    
    private ChatRoomManager() {
        this.chatRooms = new ConcurrentHashMap<>();
        logger.info("Chat Room Manager initialized");
    }
    
    public static ChatRoomManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ChatRoomManager();
                }
            }
        }
        return instance;
    }
    
    public ChatRoom createRoom(String roomId, User admin) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        
        if (admin == null) {
            throw new IllegalArgumentException("Admin user cannot be null");
        }
        
        if (chatRooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with ID '" + roomId + "' already exists");
        }
        
        ChatRoom room = new ChatRoom(roomId, admin);
        chatRooms.put(roomId, room);
        
        logger.info("Chat room created: " + roomId + " by " + admin.getUsername());
        return room;
    }
    
    public ChatRoom getRoomById(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return null;
        }
        
        return chatRooms.get(roomId);
    }
    
    public List<ChatRoom> getActiveRooms() {
        return new ArrayList<>(chatRooms.values());
    }
    
    public boolean removeRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return false;
        }
        
        ChatRoom removedRoom = chatRooms.remove(roomId);
        if (removedRoom != null) {
            logger.info("Chat room removed: " + roomId);
            return true;
        }
        
        return false;
    }
    
    public int getTotalRooms() {
        return chatRooms.size();
    }
    
    public boolean roomExists(String roomId) {
        return chatRooms.containsKey(roomId);
    }
}