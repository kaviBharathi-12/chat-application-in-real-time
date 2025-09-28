package com.smarthome.adapter;

import com.smarthome.devices.LegacyDevice;
import com.smarthome.devices.SmartDevice;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;
import com.smarthome.observer.DeviceObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ADAPTER PATTERN (Structural)
 * Adapter that allows legacy devices to work with the smart home system
 * Converts the interface of LegacyDevice to match SmartDevice interface
 */
public class LegacyDeviceAdapter implements SmartDevice {
    private static final Logger logger = LoggerFactory.getLogger(LegacyDeviceAdapter.class);
    
    private final LegacyDevice legacyDevice;
    private final List<DeviceObserver> observers;
    
    public LegacyDeviceAdapter(LegacyDevice legacyDevice) {
        if (legacyDevice == null) {
            throw new IllegalArgumentException("Legacy device cannot be null");
        }
        this.legacyDevice = legacyDevice;
        this.observers = new CopyOnWriteArrayList<>();
        logger.info("Legacy device adapter created for: " + legacyDevice.getName());
    }
    
    @Override
    public String getDeviceId() {
        return legacyDevice.getId();
    }
    
    @Override
    public String getDeviceName() {
        return legacyDevice.getName() + " (Legacy)";
    }
    
    @Override
    public boolean isOn() {
        return legacyDevice.getPowerState();
    }
    
    @Override
    public void turnOn() {
        legacyDevice.powerOn();
        logger.info("Legacy device turned ON via adapter: " + getDeviceName());
        notifyObservers();
    }
    
    @Override
    public void turnOff() {
        legacyDevice.powerOff();
        logger.info("Legacy device turned OFF via adapter: " + getDeviceName());
        notifyObservers();
    }
    
    @Override
    public String getStatus() {
        return String.format("Legacy Device Status: %s | Type: %s | Last Maintenance: %s",
            legacyDevice.getPowerState() ? "Active" : "Inactive",
            legacyDevice.getDeviceType(),
            legacyDevice.getLastMaintenanceDate());
    }
    
    @Override
    public void addObserver(DeviceObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.debug("Observer added to legacy device adapter: " + getDeviceName());
        }
    }
    
    @Override
    public void removeObserver(DeviceObserver observer) {
        if (observer != null) {
            observers.remove(observer);
            logger.debug("Observer removed from legacy device adapter: " + getDeviceName());
        }
    }
    
    @Override
    public void notifyObservers() {
        for (DeviceObserver observer : observers) {
            try {
                observer.onDeviceStateChanged(this);
            } catch (Exception e) {
                logger.error("Error notifying observer for legacy device: " + getDeviceName(), e);
            }
        }
    }
    
    @Override
    public List<DeviceObserver> getObservers() {
        return new ArrayList<>(observers);
    }
    
    public LegacyDevice getLegacyDevice() {
        return legacyDevice;
    }
    
    @Override
    public String toString() {
        return String.format("LegacyDeviceAdapter [%s - %s]", 
            getDeviceName(), isOn() ? "ON" : "OFF");
    }
}