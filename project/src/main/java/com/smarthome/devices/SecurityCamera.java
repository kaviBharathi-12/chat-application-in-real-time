package com.smarthome.devices;

import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

/**
 * Security Camera implementation with recording capabilities
 */
public class SecurityCamera extends BaseSmartDevice {
    private static final Logger logger = LoggerFactory.getLogger(SecurityCamera.class);
    
    private boolean isRecording;
    private String resolution;
    private boolean nightVisionEnabled;
    
    public SecurityCamera(String deviceId, String deviceName) {
        super(deviceId, deviceName);
        this.isRecording = false;
        this.resolution = "1080p"; // Default resolution
        this.nightVisionEnabled = true; // Default night vision enabled
        logger.debug("Security camera initialized: " + deviceName);
    }
    
    @Override
    public void turnOn() {
        super.turnOn();
        startRecording();
    }
    
    @Override
    public void turnOff() {
        stopRecording();
        super.turnOff();
    }
    
    @Override
    public String getStatus() {
        return String.format("%s | Recording: %s | Resolution: %s | Night Vision: %s", 
            super.getStatus(), 
            isRecording ? "Active" : "Inactive",
            resolution,
            nightVisionEnabled ? "Enabled" : "Disabled");
    }
    
    public void startRecording() {
        if (!isRecording) {
            isRecording = true;
            lastUpdated = java.time.LocalDateTime.now();
            logger.info("Recording started for " + deviceName);
            notifyObservers();
        }
    }
    
    public void stopRecording() {
        if (isRecording) {
            isRecording = false;
            lastUpdated = java.time.LocalDateTime.now();
            logger.info("Recording stopped for " + deviceName);
            notifyObservers();
        }
    }
    
    public void setResolution(String resolution) {
        if (resolution == null || resolution.trim().isEmpty()) {
            throw new IllegalArgumentException("Resolution cannot be null or empty");
        }
        if (!resolution.matches("720p|1080p|4K")) {
            throw new IllegalArgumentException("Invalid resolution. Use 720p, 1080p, or 4K");
        }
        this.resolution = resolution;
        lastUpdated = java.time.LocalDateTime.now();
        logger.info("Resolution changed for " + deviceName + ": " + resolution);
        notifyObservers();
    }
    
    public void setNightVision(boolean enabled) {
        this.nightVisionEnabled = enabled;
        lastUpdated = java.time.LocalDateTime.now();
        logger.info("Night vision " + (enabled ? "enabled" : "disabled") + " for " + deviceName);
        notifyObservers();
    }
    
    public boolean isRecording() {
        return isRecording;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    public boolean isNightVisionEnabled() {
        return nightVisionEnabled;
    }
}