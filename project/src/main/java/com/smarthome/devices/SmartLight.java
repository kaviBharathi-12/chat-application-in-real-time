package com.smarthome.devices;

import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

/**
 * Smart Light implementation with brightness control
 */
public class SmartLight extends BaseSmartDevice {
    private static final Logger logger = LoggerFactory.getLogger(SmartLight.class);
    
    private int brightness;
    private String color;
    
    public SmartLight(String deviceId, String deviceName) {
        super(deviceId, deviceName);
        this.brightness = 50; // Default brightness
        this.color = "Warm White"; // Default color
        logger.debug("Smart light initialized: " + deviceName);
    }
    
    @Override
    public void turnOn() {
        super.turnOn();
        if (brightness == 0) {
            brightness = 50; // Set to default brightness when turning on
        }
    }
    
    @Override
    public void turnOff() {
        super.turnOff();
        // Keep brightness setting for next time
    }
    
    @Override
    public String getStatus() {
        return String.format("%s | Brightness: %d%% | Color: %s", 
            super.getStatus(), brightness, color);
    }
    
    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("Brightness must be between 0 and 100");
        }
        this.brightness = brightness;
        lastUpdated = java.time.LocalDateTime.now();
        logger.info("Brightness changed for " + deviceName + ": " + brightness + "%");
        notifyObservers();
    }
    
    public void setColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }
        this.color = color;
        lastUpdated = java.time.LocalDateTime.now();
        logger.info("Color changed for " + deviceName + ": " + color);
        notifyObservers();
    }
    
    public int getBrightness() {
        return brightness;
    }
    
    public String getColor() {
        return color;
    }
}