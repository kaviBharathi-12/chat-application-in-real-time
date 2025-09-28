package com.smarthome.exceptions;

/**
 * Exception thrown when system-level errors occur
 */
public class SystemException extends Exception {
    
    public SystemException(String message) {
        super(message);
    }
    
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}