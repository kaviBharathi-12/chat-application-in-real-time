package com.smarthome.command;

/**
 * COMMAND PATTERN (Behavioral)
 * Interface for all commands that can be executed in the smart home system
 * Encapsulates requests as objects and allows for queuing, logging, and undo operations
 */
public interface Command {
    void execute();
    String getDescription();
    long getExecutionTime();
    boolean isSuccessful();
}