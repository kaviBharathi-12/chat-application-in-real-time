package com.chatapp.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Console-based logger implementation
 * Provides formatted output with timestamps and log levels
 */
public class ConsoleLogger implements Logger {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private final String name;
    private final boolean debugEnabled;
    private final boolean infoEnabled;
    
    public ConsoleLogger(String name) {
        this.name = name != null ? name : "UNKNOWN";
        this.debugEnabled = true; // Can be configured via system properties
        this.infoEnabled = true;
    }
    
    @Override
    public void info(String message) {
        if (infoEnabled) {
            log("INFO", message, null);
        }
    }
    
    @Override
    public void debug(String message) {
        if (debugEnabled) {
            log("DEBUG", message, null);
        }
    }
    
    @Override
    public void warn(String message) {
        log("WARN", message, null);
    }
    
    @Override
    public void error(String message) {
        log("ERROR", message, null);
    }
    
    @Override
    public void error(String message, Throwable throwable) {
        log("ERROR", message, throwable);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }
    
    @Override
    public boolean isInfoEnabled() {
        return infoEnabled;
    }
    
    private void log(String level, String message, Throwable throwable) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logMessage = String.format("[%s] %-5s %s - %s", 
            timestamp, level, getSimpleClassName(name), message);
        
        System.out.println(logMessage);
        
        if (throwable != null) {
            throwable.printStackTrace(System.out);
        }
    }
    
    private String getSimpleClassName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
    }
}