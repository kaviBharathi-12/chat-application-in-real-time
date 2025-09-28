package com.chatapp.service;

import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SINGLETON PATTERN (Creational)
 * Manages user creation and retrieval
 * Ensures centralized user management
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static volatile UserService instance;
    private static final Object lock = new Object();
    
    private final ConcurrentHashMap<String, User> users;
    
    private UserService() {
        this.users = new ConcurrentHashMap<>();
        logger.info("User Service initialized");
    }
    
    public static UserService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }
    
    public User createOrGetUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        String normalizedUsername = username.trim().toLowerCase();
        
        return users.computeIfAbsent(normalizedUsername, key -> {
            User newUser = new User(username.trim());
            logger.info("New user created: " + username);
            return newUser;
        });
    }
    
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        return users.get(username.trim().toLowerCase());
    }
    
    public boolean userExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return users.containsKey(username.trim().toLowerCase());
    }
    
    public int getTotalUsers() {
        return users.size();
    }
    
    public void removeUser(String username) {
        if (username != null && !username.trim().isEmpty()) {
            User removedUser = users.remove(username.trim().toLowerCase());
            if (removedUser != null) {
                removedUser.setOnline(false);
                logger.info("User removed: " + username);
            }
        }
    }
}