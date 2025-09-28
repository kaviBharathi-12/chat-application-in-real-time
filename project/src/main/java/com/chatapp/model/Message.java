package com.chatapp.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a message in the chat system
 * Supports both public and private messages
 */
public class Message {
    private final String messageId;
    private final User sender;
    private final String content;
    private final LocalDateTime timestamp;
    private final User recipient; // null for public messages
    private final boolean isPrivate;
    
    // Constructor for public messages
    public Message(User sender, String content) {
        this(sender, content, null);
    }
    
    // Constructor for private messages
    public Message(User sender, String content, User recipient) {
        if (sender == null) {
            throw new IllegalArgumentException("Sender cannot be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }
        
        this.messageId = generateMessageId();
        this.sender = sender;
        this.content = content.trim();
        this.timestamp = LocalDateTime.now();
        this.recipient = recipient;
        this.isPrivate = (recipient != null);
    }
    
    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // Getters
    public String getMessageId() {
        return messageId;
    }
    
    public User getSender() {
        return sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public User getRecipient() {
        return recipient;
    }
    
    public boolean isPrivate() {
        return isPrivate;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return Objects.equals(messageId, message.messageId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
    
    @Override
    public String toString() {
        if (isPrivate) {
            return String.format("PrivateMessage[%s -> %s: %s]", 
                               sender.getUsername(), recipient.getUsername(), content);
        } else {
            return String.format("Message[%s: %s]", sender.getUsername(), content);
        }
    }
}