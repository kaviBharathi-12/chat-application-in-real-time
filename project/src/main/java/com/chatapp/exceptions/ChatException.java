package com.chatapp.exceptions;

/**
 * Custom exception for chat application errors
 * Provides specific error handling for chat-related operations
 */
public class ChatException extends Exception {
    
    public ChatException(String message) {
        super(message);
    }
    
    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }
}