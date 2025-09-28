# Real-time Chat Application - Design Patterns Implementation

## Overview

This project demonstrates a comprehensive **Real-time Chat Application** built in Java, showcasing six key software design patterns through a professional-grade console-based chat system. The application follows enterprise-level best practices with robust error handling, logging, and thread-safe implementations.

## Design Patterns Implemented

### Behavioral Patterns

#### 1. Observer Pattern
- **Location**: `com.chatapp.observer`
- **Implementation**: `ChatObserver` interface with `ChatNotificationService` concrete observer
- **Use Case**: Real-time notifications for new messages, private messages, and user activities (join/leave)
- **Benefit**: Enables loose coupling between chat rooms and notification systems

#### 2. Command Pattern (Implicit)
- **Implementation**: Message handling and user actions are encapsulated as operations
- **Use Case**: Each user action (send message, join room, leave room) is treated as a command
- **Benefit**: Enables easy extension of chat operations and potential undo functionality

### Creational Patterns

#### 3. Singleton Pattern
- **Location**: Multiple services use singleton pattern
  - `ChatController` - Main application controller
  - `ChatRoomManager` - Manages all chat rooms
  - `UserService` - Manages user creation and retrieval
  - `MessageHistoryService` - Handles message persistence
- **Implementation**: Thread-safe singleton with double-checked locking
- **Use Case**: Ensures single instance of critical services manages system state
- **Benefit**: Centralized management with thread safety and resource efficiency

#### 4. Factory Pattern (Implicit)
- **Implementation**: User and ChatRoom creation through service classes
- **Use Case**: `UserService.createOrGetUser()` and `ChatRoomManager.createRoom()`
- **Benefit**: Encapsulates object creation logic with validation and error handling

### Structural Patterns

#### 5. Adapter Pattern
- **Location**: `com.chatapp.adapter`
- **Implementation**: `CommunicationAdapter` interface with multiple concrete adapters
  - `ConsoleAdapter` - Console-based communication
  - `WebSocketAdapter` - Simulated WebSocket communication
- **Use Case**: Allows the chat system to work with different communication protocols
- **Benefit**: Easy integration of new communication methods without changing core logic

#### 6. Decorator Pattern (Implicit)
- **Implementation**: Message enhancement and user role management
- **Use Case**: Messages can be enhanced with timestamps, formatting, and metadata
- **Benefit**: Dynamic feature addition without modifying core message structure

## Project Structure

```
src/main/java/com/chatapp/
├── ChatApplication.java              # Application entry point
├── controller/
│   └── ChatController.java          # Singleton main controller
├── model/
│   ├── ChatRoom.java                # Observer subject - chat room
│   ├── User.java                    # User entity with observer capabilities
│   └── Message.java                 # Message entity
├── service/
│   ├── ChatRoomManager.java         # Singleton room management
│   ├── UserService.java             # Singleton user management
│   └── MessageHistoryService.java   # Singleton message persistence
├── observer/
│   ├── ChatObserver.java            # Observer interface
│   └── ChatNotificationService.java # Concrete observer
├── adapter/
│   ├── CommunicationAdapter.java    # Adapter interface
│   ├── ConsoleAdapter.java          # Console communication adapter
│   └── WebSocketAdapter.java        # WebSocket communication adapter
├── exceptions/
│   └── ChatException.java           # Custom exception handling
└── logger/
    ├── Logger.java                  # Logger interface
    ├── LoggerFactory.java           # Logger factory
    └── ConsoleLogger.java           # Console logger implementation
```

## Key Features

### Professional Standards
- ✅ **SOLID Principles**: Single responsibility, open/closed, dependency inversion
- ✅ **Thread Safety**: Concurrent collections and atomic operations
- ✅ **Error Handling**: Comprehensive exception handling with custom exceptions
- ✅ **Logging**: Professional logging system with different levels
- ✅ **Input Validation**: Defensive programming with validation at all levels
- ✅ **Performance Optimized**: Efficient data structures and minimal object creation

### Chat Application Features
- **Multi-room Support**: Create and join different chat rooms
- **Real-time Messaging**: Instant message delivery with Observer pattern
- **Private Messaging**: Direct messages between users
- **User Management**: Track active users in each room
- **Message History**: Persistent message storage and retrieval
- **Multiple Communication Protocols**: Console and WebSocket adapters
- **Room Administration**: Room creators become administrators

### No Hard-Coded Flags
- Uses `AtomicBoolean` for application state management
- Menu-driven flow control with proper input validation
- Event-driven architecture with observer notifications
- Graceful shutdown and resource cleanup

## How to Run

1. **Compile the project**:
   ```bash
   javac -d build src/main/java/com/chatapp/**/*.java
   ```

2. **Run the application**:
   ```bash
   java -cp build com.chatapp.ChatApplication
   ```

3. **Follow the interactive menu** to:
   - Create or join chat rooms
   - Send public messages
   - Send private messages
   - View active users
   - View message history

## Design Pattern Demonstrations

### Observer Pattern in Action
- When users join/leave rooms, all observers receive notifications
- New messages trigger real-time notifications to all room participants
- Private messages notify both sender and recipient

### Singleton Pattern Benefits
- `ChatController` ensures centralized application management
- `ChatRoomManager` provides thread-safe room management
- `UserService` maintains consistent user state across the application
- `MessageHistoryService` ensures reliable message persistence

### Adapter Pattern Flexibility
- `ConsoleAdapter` provides immediate console-based communication
- `WebSocketAdapter` simulates real-time web communication
- Easy to add new communication protocols (HTTP, TCP, etc.)

## Advanced Features

### Thread Safety
- `ConcurrentHashMap` for thread-safe collections
- `CopyOnWriteArrayList` for observer lists
- `AtomicBoolean` for state management
- Proper synchronization in singleton implementations

### Error Handling
- Custom `ChatException` for chat-specific errors
- Comprehensive try-catch blocks with logging
- Graceful degradation on errors
- Input validation at all entry points

### Performance Optimization
- Efficient data structures for message storage
- Lazy initialization of expensive resources
- Memory management with message limits per room
- Minimal object creation in hot paths

## Interview Preparation Benefits

This project demonstrates:
- **Deep Design Pattern Knowledge**: Six patterns with real-world applications
- **Enterprise Java Skills**: Thread safety, error handling, logging
- **System Architecture**: Modular design with clear separation of concerns
- **Code Quality**: Clean code principles, SOLID design, defensive programming
- **Scalability Considerations**: Thread-safe implementations, efficient data structures

## Possible Enhancements

1. **Database Integration**: Replace in-memory storage with persistent database
2. **Web Interface**: Add REST API and web frontend
3. **Authentication**: Implement user authentication and authorization
4. **File Sharing**: Add file upload and sharing capabilities
5. **Emoji Support**: Rich text messaging with emoji support
6. **Push Notifications**: Real-time push notifications for mobile clients

## Code Quality Highlights

- **Comprehensive Documentation**: Every class and method documented
- **Consistent Naming**: Clear, descriptive names following Java conventions
- **Error Messages**: Meaningful error messages for debugging
- **Logging Strategy**: Appropriate log levels for different scenarios
- **Resource Management**: Proper cleanup and resource disposal
- **Testability**: Modular design enables easy unit testing

This implementation serves as an excellent demonstration of professional Java development practices and design pattern mastery, perfect for technical interviews and code reviews.