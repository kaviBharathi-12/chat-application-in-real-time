package com.smarthome.logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for creating logger instances
 * Maintains a cache of loggers to avoid creating duplicates
 */
public class LoggerFactory {
    private static final ConcurrentHashMap<String, Logger> loggerCache = new ConcurrentHashMap<>();
    
    private LoggerFactory() {
        // Private constructor to prevent instantiation
    }
    
    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        return getLogger(clazz.getName());
    }
    
    public static Logger getLogger(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Logger name cannot be null or empty");
        }
        
        return loggerCache.computeIfAbsent(name, ConsoleLogger::new);
    }
}