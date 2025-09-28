package com.chatapp.adapter;

import com.chatapp.model.Message;
import com.chatapp.model.User;

/**
 * ADAPTER PATTERN (Structural)
 * Interface for different communication protocols
 * Allows the system to work with various client communication methods
 */
public interface CommunicationAdapter {
    void sendMessage(Message message);
    void notifyUserJoined(User user, String roomId);
    void notifyUserLeft(User user, String roomId);
    void displaySystemMessage(String message);
    boolean isConnected();
    void connect();
    void disconnect();
    String getProtocolType();
}