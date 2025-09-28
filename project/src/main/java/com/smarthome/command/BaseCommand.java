package com.smarthome.command;

import com.smarthome.devices.SmartDevice;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

/**
 * Base implementation for commands with common functionality
 */
public abstract class BaseCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(BaseCommand.class);
    
    protected final SmartDevice device;
    protected long executionTime;
    protected boolean successful;
    
    protected BaseCommand(SmartDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("Device cannot be null");
        }
        this.device = device;
        this.successful = false;
        this.executionTime = 0;
    }
    
    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        try {
            executeCommand();
            successful = true;
            logger.debug("Command executed successfully: " + getDescription());
        } catch (Exception e) {
            successful = false;
            logger.error("Command execution failed: " + getDescription(), e);
        } finally {
            executionTime = System.currentTimeMillis() - startTime;
        }
    }
    
    protected abstract void executeCommand();
    
    @Override
    public long getExecutionTime() {
        return executionTime;
    }
    
    @Override
    public boolean isSuccessful() {
        return successful;
    }
    
    protected SmartDevice getDevice() {
        return device;
    }
}