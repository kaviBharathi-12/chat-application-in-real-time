package com.smarthome.command;

import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * COMMAND PATTERN (Behavioral)
 * Invoker class that executes commands and maintains command history
 * Demonstrates command queuing and execution management
 */
public class CommandInvoker {
    private static final Logger logger = LoggerFactory.getLogger(CommandInvoker.class);
    
    private final List<CommandExecution> commandHistory;
    
    public CommandInvoker() {
        this.commandHistory = new CopyOnWriteArrayList<>();
        logger.info("Command Invoker initialized");
    }
    
    public void executeCommand(Command command) {
        if (command == null) {
            logger.warn("Attempted to execute null command");
            return;
        }
        
        try {
            logger.info("Executing command: " + command.getDescription());
            
            long startTime = System.currentTimeMillis();
            command.execute();
            
            CommandExecution execution = new CommandExecution(
                command, LocalDateTime.now(), command.isSuccessful()
            );
            commandHistory.add(execution);
            
            if (command.isSuccessful()) {
                logger.info("Command completed successfully: " + command.getDescription() + 
                           " (Time: " + command.getExecutionTime() + "ms)");
            } else {
                logger.error("Command failed: " + command.getDescription());
            }
            
        } catch (Exception e) {
            logger.error("Error executing command: " + command.getDescription(), e);
            CommandExecution execution = new CommandExecution(
                command, LocalDateTime.now(), false
            );
            commandHistory.add(execution);
        }
    }
    
    public void executeBatchCommands(List<Command> commands) {
        if (commands == null || commands.isEmpty()) {
            logger.warn("No commands provided for batch execution");
            return;
        }
        
        logger.info("Executing batch of " + commands.size() + " commands");
        
        int successCount = 0;
        long totalTime = System.currentTimeMillis();
        
        for (Command command : commands) {
            executeCommand(command);
            if (command.isSuccessful()) {
                successCount++;
            }
        }
        
        totalTime = System.currentTimeMillis() - totalTime;
        
        logger.info(String.format("Batch execution completed: %d/%d successful (Total time: %dms)",
            successCount, commands.size(), totalTime));
    }
    
    public List<CommandExecution> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    public void printCommandHistory() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("COMMAND EXECUTION HISTORY");
        System.out.println("=".repeat(50));
        
        if (commandHistory.isEmpty()) {
            System.out.println("No commands have been executed yet.");
            return;
        }
        
        commandHistory.forEach(execution -> {
            String timestamp = execution.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String status = execution.isSuccessful() ? "✓" : "✗";
            System.out.printf("[%s] %s %s%n", timestamp, status, execution.getCommand().getDescription());
        });
        
        long totalCommands = commandHistory.size();
        long successfulCommands = commandHistory.stream().mapToLong(e -> e.isSuccessful() ? 1 : 0).sum();
        
        System.out.println("-".repeat(50));
        System.out.printf("Total Commands: %d | Successful: %d | Failed: %d%n",
            totalCommands, successfulCommands, totalCommands - successfulCommands);
    }
    
    public void clearHistory() {
        commandHistory.clear();
        logger.info("Command history cleared");
    }
    
    /**
     * Inner class to represent a command execution record
     */
    public static class CommandExecution {
        private final Command command;
        private final LocalDateTime timestamp;
        private final boolean successful;
        
        public CommandExecution(Command command, LocalDateTime timestamp, boolean successful) {
            this.command = command;
            this.timestamp = timestamp;
            this.successful = successful;
        }
        
        public Command getCommand() {
            return command;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public boolean isSuccessful() {
            return successful;
        }
    }
}