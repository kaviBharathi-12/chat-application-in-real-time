package com.smarthome.devices;

import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

/**
 * Smart Thermostat implementation with temperature control
 */
public class SmartThermostat extends BaseSmartDevice {
    private static final Logger logger = LoggerFactory.getLogger(SmartThermostat.class);
    
    private double targetTemperature;
    private double currentTemperature;
    private String mode;
    
    public SmartThermostat(String deviceId, String deviceName) {
        super(deviceId, deviceName);
        this.targetTemperature = 22.0; // Default target temperature in Celsius
        this.currentTemperature = 20.0; // Simulated current temperature
        this.mode = "AUTO"; // Default mode
        logger.debug("Smart thermostat initialized: " + deviceName);
    }
    
    @Override
    public String getStatus() {
        return String.format("%s | Target: %.1f°C | Current: %.1f°C | Mode: %s", 
            super.getStatus(), targetTemperature, currentTemperature, mode);
    }
    
    public void setTargetTemperature(double temperature) {
        if (temperature < 5.0 || temperature > 35.0) {
            throw new IllegalArgumentException("Target temperature must be between 5°C and 35°C");
        }
        this.targetTemperature = temperature;
        lastUpdated = java.time.LocalDateTime.now();
        logger.info("Target temperature changed for " + deviceName + ": " + temperature + "°C");
        simulateTemperatureAdjustment();
        notifyObservers();
    }
    
    public void setMode(String mode) {
        if (mode == null || mode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mode cannot be null or empty");
        }
        String upperMode = mode.toUpperCase();
        if (!upperMode.matches("AUTO|HEAT|COOL|OFF")) {
            throw new IllegalArgumentException("Invalid mode. Use AUTO, HEAT, COOL, or OFF");
        }
        this.mode = upperMode;
        lastUpdated = java.time.LocalDateTime.now();
        logger.info("Mode changed for " + deviceName + ": " + mode);
        notifyObservers();
    }
    
    private void simulateTemperatureAdjustment() {
        // Simulate gradual temperature change towards target
        if (currentTemperature < targetTemperature) {
            currentTemperature = Math.min(targetTemperature, currentTemperature + 0.5);
        } else if (currentTemperature > targetTemperature) {
            currentTemperature = Math.max(targetTemperature, currentTemperature - 0.5);
        }
    }
    
    public double getTargetTemperature() {
        return targetTemperature;
    }
    
    public double getCurrentTemperature() {
        return currentTemperature;
    }
    
    public String getMode() {
        return mode;
    }
}