package com.chatapp.controller;

import com.chatapp.adapter.CommunicationAdapter;
import com.chatapp.adapter.ConsoleAdapter;
import com.chatapp.exceptions.ChatException;
import com.chatapp.logger.Logger;
import com.chatapp.logger.LoggerFactory;
import com.chatapp.model.ChatRoom;
import com.chatapp.model.Message;
import com.chatapp.model.User;
import com.chatapp.service.ChatRoomManager;
import com.chatapp.service.MessageHistoryService;
import com.chatapp.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SINGLETON PATTERN (Creational)
 * Main controller for the chat application
 * Ensures single instance manages the entire chat system
 */
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private static volatile ChatController instance;
    private static final Object lock = new Object();
    
    private final ChatRoomManager chatRoomManager;
    private final UserService userService;
    private final MessageHistoryService messageHistoryService;
    private final CommunicationAdapter communicationAdapter;
    private final AtomicBoolean applicationRunning;
    private final Scanner scanner;
    
    private User currentUser;
    private ChatRoom currentRoom;
    
    private ChatController() {
        this.chatRoomManager = ChatRoomManager.getInstance();
        this.userService = UserService.getInstance();
        this.messageHistoryService = MessageHistoryService.getInstance();
        this.communicationAdapter = new ConsoleAdapter();
        this.applicationRunning = new AtomicBoolean(false);
        this.scanner = new Scanner(System.in);
        logger.info("Chat Controller initialized");
    }
    
    public static ChatController getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ChatController();
                }
            }
        }
        return instance;
    }
    
    public void startApplication() throws ChatException {
        try {
            applicationRunning.set(true);
            showWelcomeMessage();
            authenticateUser();
            runMainLoop();
        } catch (Exception e) {
            logger.error("Failed to start chat application", e);
            throw new ChatException("Application startup failed: " + e.getMessage());
        }
    }
    
    private void showWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   🚀 REAL-TIME CHAT APPLICATION 🚀");
        System.out.println("=".repeat(60));
        System.out.println("Welcome to the most advanced chat system!");
        System.out.println("Connect, Chat, and Collaborate in real-time");
    }
    
    private void authenticateUser() {
        System.out.print("\n👤 Enter your username: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("❌ Username cannot be empty!");
            authenticateUser();
            return;
        }
        
        currentUser = userService.createOrGetUser(username);
        System.out.println("✅ Welcome, " + currentUser.getUsername() + "!");
        logger.info("User authenticated: " + username);
    }
    
    private void runMainLoop() {
        while (applicationRunning.get()) {
            try {
                if (currentRoom == null) {
                    showRoomMenu();
                } else {
                    showChatMenu();
                }
            } catch (Exception e) {
                logger.error("Error in main loop", e);
                System.out.println("❌ An error occurred. Please try again.");
            }
        }
        cleanup();
    }
    
    private void showRoomMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("🏠 ROOM MANAGEMENT");
        System.out.println("-".repeat(40));
        System.out.println("1. Join Existing Room");
        System.out.println("2. Create New Room");
        System.out.println("3. List Active Rooms");
        System.out.println("4. Exit Application");
        System.out.print("Choose option (1-4): ");
        
        int choice = getValidChoice(1, 4);
        processRoomChoice(choice);
    }
    
    private void showChatMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("💬 CHAT ROOM: " + currentRoom.getRoomId());
        System.out.println("-".repeat(40));
        System.out.println("1. Send Message");
        System.out.println("2. View Recent Messages");
        System.out.println("3. View Active Users");
        System.out.println("4. Send Private Message");
        System.out.println("5. Leave Room");
        System.out.print("Choose option (1-5): ");
        
        int choice = getValidChoice(1, 5);
        processChatChoice(choice);
    }
    
    private int getValidChoice(int min, int max) {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < min || choice > max) {
                System.out.println("❌ Invalid choice. Please enter " + min + "-" + max + ".");
                return getValidChoice(min, max);
            }
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
            return getValidChoice(min, max);
        }
    }
    
    private void processRoomChoice(int choice) {
        switch (choice) {
            case 1 -> joinExistingRoom();
            case 2 -> createNewRoom();
            case 3 -> listActiveRooms();
            case 4 -> {
                applicationRunning.set(false);
                System.out.println("👋 Goodbye! Thanks for using our chat application.");
            }
        }
    }
    
    private void processChatChoice(int choice) {
        switch (choice) {
            case 1 -> sendMessage();
            case 2 -> viewRecentMessages();
            case 3 -> viewActiveUsers();
            case 4 -> sendPrivateMessage();
            case 5 -> leaveRoom();
        }
    }
    
    private void joinExistingRoom() {
        System.out.print("🔑 Enter Room ID: ");
        String roomId = scanner.nextLine().trim();
        
        if (roomId.isEmpty()) {
            System.out.println("❌ Room ID cannot be empty!");
            return;
        }
        
        try {
            ChatRoom room = chatRoomManager.getRoomById(roomId);
            if (room == null) {
                System.out.println("❌ Room '" + roomId + "' not found!");
                return;
            }
            
            room.addUser(currentUser);
            currentRoom = room;
            System.out.println("✅ Successfully joined room: " + roomId);
            
            // Show message history
            List<Message> history = messageHistoryService.getMessageHistory(roomId);
            if (!history.isEmpty()) {
                System.out.println("\n📜 Recent Messages:");
                history.stream().limit(5).forEach(this::displayMessage);
            }
            
        } catch (Exception e) {
            logger.error("Error joining room: " + roomId, e);
            System.out.println("❌ Failed to join room: " + e.getMessage());
        }
    }
    
    private void createNewRoom() {
        System.out.print("🆕 Enter new Room ID: ");
        String roomId = scanner.nextLine().trim();
        
        if (roomId.isEmpty()) {
            System.out.println("❌ Room ID cannot be empty!");
            return;
        }
        
        try {
            ChatRoom room = chatRoomManager.createRoom(roomId, currentUser);
            currentRoom = room;
            System.out.println("✅ Room '" + roomId + "' created successfully!");
            System.out.println("🎉 You are now the room administrator.");
            
        } catch (Exception e) {
            logger.error("Error creating room: " + roomId, e);
            System.out.println("❌ Failed to create room: " + e.getMessage());
        }
    }
    
    private void listActiveRooms() {
        List<ChatRoom> activeRooms = chatRoomManager.getActiveRooms();
        
        if (activeRooms.isEmpty()) {
            System.out.println("📭 No active rooms available.");
            return;
        }
        
        System.out.println("\n🏠 Active Rooms:");
        System.out.println("-".repeat(50));
        for (ChatRoom room : activeRooms) {
            System.out.printf("🏠 %-15s | 👥 %d users | 📅 Created: %s%n",
                room.getRoomId(),
                room.getActiveUsers().size(),
                room.getCreatedAt().toString().substring(0, 19));
        }
    }
    
    private void sendMessage() {
        System.out.print("💬 Enter your message: ");
        String content = scanner.nextLine().trim();
        
        if (content.isEmpty()) {
            System.out.println("❌ Message cannot be empty!");
            return;
        }
        
        try {
            Message message = new Message(currentUser, content);
            currentRoom.broadcastMessage(message);
            messageHistoryService.saveMessage(currentRoom.getRoomId(), message);
            
            System.out.println("✅ Message sent successfully!");
            
        } catch (Exception e) {
            logger.error("Error sending message", e);
            System.out.println("❌ Failed to send message: " + e.getMessage());
        }
    }
    
    private void viewRecentMessages() {
        try {
            List<Message> messages = messageHistoryService.getMessageHistory(currentRoom.getRoomId());
            
            if (messages.isEmpty()) {
                System.out.println("📭 No messages in this room yet.");
                return;
            }
            
            System.out.println("\n📜 Recent Messages (Last 10):");
            System.out.println("-".repeat(50));
            messages.stream().limit(10).forEach(this::displayMessage);
            
        } catch (Exception e) {
            logger.error("Error retrieving messages", e);
            System.out.println("❌ Failed to retrieve messages: " + e.getMessage());
        }
    }
    
    private void viewActiveUsers() {
        List<User> activeUsers = currentRoom.getActiveUsers();
        
        System.out.println("\n👥 Active Users (" + activeUsers.size() + "):");
        System.out.println("-".repeat(30));
        for (User user : activeUsers) {
            String status = user.equals(currentUser) ? " (You)" : "";
            String adminStatus = user.equals(currentRoom.getAdmin()) ? " 👑" : "";
            System.out.println("👤 " + user.getUsername() + status + adminStatus);
        }
    }
    
    private void sendPrivateMessage() {
        List<User> activeUsers = currentRoom.getActiveUsers();
        activeUsers.remove(currentUser); // Remove self from list
        
        if (activeUsers.isEmpty()) {
            System.out.println("❌ No other users available for private messaging.");
            return;
        }
        
        System.out.println("\n👥 Available Users:");
        for (int i = 0; i < activeUsers.size(); i++) {
            System.out.println((i + 1) + ". " + activeUsers.get(i).getUsername());
        }
        
        System.out.print("Select user (1-" + activeUsers.size() + "): ");
        int userChoice = getValidChoice(1, activeUsers.size());
        User recipient = activeUsers.get(userChoice - 1);
        
        System.out.print("💌 Enter private message: ");
        String content = scanner.nextLine().trim();
        
        if (content.isEmpty()) {
            System.out.println("❌ Message cannot be empty!");
            return;
        }
        
        try {
            Message privateMessage = new Message(currentUser, content, recipient);
            currentRoom.sendPrivateMessage(privateMessage);
            
            System.out.println("✅ Private message sent to " + recipient.getUsername());
            
        } catch (Exception e) {
            logger.error("Error sending private message", e);
            System.out.println("❌ Failed to send private message: " + e.getMessage());
        }
    }
    
    private void leaveRoom() {
        try {
            currentRoom.removeUser(currentUser);
            System.out.println("👋 Left room: " + currentRoom.getRoomId());
            currentRoom = null;
            
        } catch (Exception e) {
            logger.error("Error leaving room", e);
            System.out.println("❌ Failed to leave room: " + e.getMessage());
        }
    }
    
    private void displayMessage(Message message) {
        String timestamp = message.getTimestamp().toString().substring(11, 19);
        if (message.isPrivate()) {
            System.out.printf("[%s] 💌 %s -> %s: %s%n",
                timestamp,
                message.getSender().getUsername(),
                message.getRecipient().getUsername(),
                message.getContent());
        } else {
            System.out.printf("[%s] %s: %s%n",
                timestamp,
                message.getSender().getUsername(),
                message.getContent());
        }
    }
    
    private void cleanup() {
        try {
            if (currentRoom != null) {
                currentRoom.removeUser(currentUser);
            }
            scanner.close();
            logger.info("Chat application shutdown completed");
            System.out.println("🔒 Application closed successfully. Goodbye!");
        } catch (Exception e) {
            logger.error("Error during cleanup", e);
        }
    }
}