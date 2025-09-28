package com.smarthome.devices;

import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Legacy device that doesn't implement SmartDevice interface
 * Used to demonstrate Adapter pattern integration
 */
public class LegacyDevice {
    private static final Logger logger = LoggerFactory.getLogger(LegacyDevice.class);
    
    private final String id;
    private final String name;
    private final String deviceType;
    private boolean powerState;
    private final LocalDate lastMaintenanceDate;
    
    public LegacyDevice(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Device ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Device name cannot be null or empty");
        }
        
        this.id = id;
        this.name = name;
        this.deviceType = "Legacy Equipment";
        this.powerState = false;
        this.lastMaintenanceDate = LocalDate.now().minusDays(30); // 30 days ago
        
        logger.debug("Legacy device created: " + name);
    }
    
    public void powerOn() {
        if (!powerState) {
            powerState = true;
            logger.info("Legacy device powered ON: " + name);
        }
    }
    
    public void powerOff() {
        if (powerState) {
            powerState = false;
            logger.info("Legacy device powered OFF: " + name);
        }
    }
    
    public boolean getPowerState() {
        return powerState;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public String getLastMaintenanceDate() {
        return lastMaintenanceDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public void performMaintenance() {
        logger.info("Performing maintenance on legacy device: " + name);
        // Simulate maintenance operations
    }
    
    @Override
    public String toString() {
        return String.format("LegacyDevice [ID: %s, Name: %s, State: %s]", 
            id, name, powerState ? "ON" : "OFF");
    }
}