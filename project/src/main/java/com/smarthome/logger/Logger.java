package com.smarthome.logger;

/**
 * Logger interface for the smart home system
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