package com.chatapp.observer;

import com.chatapp.model.ChatRoom;
import com.chatapp.model.Message;
import com.chatapp.model.User;

/**
 * OBSERVER PATTERN (Behavioral)
 * Interface for observers that want to be notified of chat events
 * Enables real-time notifications for messages and user activities
 */
public interface ChatObserver {
    void onMessageReceived(ChatRoom room, Message message);
    void onPrivateMessageReceived(ChatRoom room, Message message);
    void onUserJoined(ChatRoom room, User user);
    void onUserLeft(ChatRoom room, User user);
}