package com.chatapp.logger;

/**
 * Logger interface for the chat application
 * Provides different logging levels and error handling
 */
public interface Logger {
    void info(String message);
    void debug(String message);
    void warn(String message);
    void error(String message);
    void error(String message, Throwable throwable);
    boolean isDebugEnabled();
    boolean isInfoEnabled();
}