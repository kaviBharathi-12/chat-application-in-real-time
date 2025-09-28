package com.chatapp;

import com.chatapp.controller.ChatController;
import com.chatapp.exceptions.ChatException;
import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;

/**
 * Main entry point for Real-time Chat Application
 * Demonstrates comprehensive design patterns implementation
 */
public class ChatApplication {
    private static final Logger logger = LoggerFactory.getLogger(ChatApplication.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Starting Real-time Chat Application...");
            
            ChatController controller = ChatController.getInstance();
            controller.startApplication();
            
        } catch (ChatException e) {
            logger.error("Chat application failed to start: " + e.getMessage(), e);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Unexpected error occurred: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}