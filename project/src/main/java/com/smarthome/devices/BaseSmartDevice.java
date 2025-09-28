package com.smarthome.devices;

import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;
import com.smarthome.observer.DeviceObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base implementation for smart devices with common functionality
 * Implements Observer pattern for device state notifications
 */
public abstract class BaseSmartDevice implements SmartDevice {
    private static final Logger logger = LoggerFactory.getLogger(BaseSmartDevice.class);
    
    protected final String deviceId;
    protected final String deviceName;
    protected boolean isOn;
    protected final List<DeviceObserver> observers;
    protected LocalDateTime lastUpdated;
    
    protected BaseSmartDevice(String deviceId, String deviceName) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Device ID cannot be null or empty");
        }
        if (deviceName == null || deviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Device name cannot be null or empty");
        }
        
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.isOn = false;
        this.observers = new CopyOnWriteArrayList<>(); // Thread-safe
        this.lastUpdated = LocalDateTime.now();
        
        logger.debug("Base smart device created: " + deviceName);
    }
    
    @Override
    public String getDeviceId() {
        return deviceId;
    }
    
    @Override
    public String getDeviceName() {
        return deviceName;
    }
    
    @Override
    public boolean isOn() {
        return isOn;
    }
    
    @Override
    public void turnOn() {
        if (!isOn) {
            isOn = true;
            lastUpdated = LocalDateTime.now();
            logger.info("Device turned ON: " + deviceName);
            notifyObservers();
        }
    }
    
    @Override
    public void turnOff() {
        if (isOn) {
            isOn = false;
            lastUpdated = LocalDateTime.now();
            logger.info("Device turned OFF: " + deviceName);
            notifyObservers();
        }
    }
    
    @Override
    public String getStatus() {
        return String.format("%s (Updated: %s)", 
            isOn ? "Active" : "Inactive",
            lastUpdated.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    
    @Override
    public void addObserver(DeviceObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.debug("Observer added to device: " + deviceName);
        }
    }
    
    @Override
    public void removeObserver(DeviceObserver observer) {
        if (observer != null) {
            observers.remove(observer);
            logger.debug("Observer removed from device: " + deviceName);
        }
    }
    
    @Override
    public void notifyObservers() {
        for (DeviceObserver observer : observers) {
            try {
                observer.onDeviceStateChanged(this);
            } catch (Exception e) {
                logger.error("Error notifying observer for device: " + deviceName, e);
            }
        }
    }
    
    @Override
    public List<DeviceObserver> getObservers() {
        return new ArrayList<>(observers);
    }
    
    @Override
    public String toString() {
        return String.format("%s [ID: %s, Status: %s]", 
            deviceName, deviceId, isOn ? "ON" : "OFF");
    }
}