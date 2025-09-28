package com.smarthome.observer;

import com.smarthome.devices.SmartDevice;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OBSERVER PATTERN (Behavioral)
 * Concrete observer that monitors device status changes and maintains statistics
 * Demonstrates how observers react to subject state changes
 */
public class DeviceStatusMonitor implements DeviceObserver {
    private static final Logger logger = LoggerFactory.getLogger(DeviceStatusMonitor.class);
    
    private final Map<String, Integer> deviceStateChanges;
    private final Map<String, LocalDateTime> lastUpdateTimes;
    
    public DeviceStatusMonitor() {
        this.deviceStateChanges = new ConcurrentHashMap<>();
        this.lastUpdateTimes = new ConcurrentHashMap<>();
        logger.info("Device Status Monitor initialized");
    }
    
    public void addDevice(SmartDevice device) {
        if (device == null) {
            logger.warn("Attempted to add null device to monitor");
            return;
        }
        
        device.addObserver(this);
        deviceStateChanges.put(device.getDeviceId(), 0);
        lastUpdateTimes.put(device.getDeviceId(), LocalDateTime.now());
        
        logger.info("Device added to monitoring: " + device.getDeviceName());
    }
    
    public void removeDevice(SmartDevice device) {
        if (device == null) {
            return;
        }
        
        device.removeObserver(this);
        deviceStateChanges.remove(device.getDeviceId());
        lastUpdateTimes.remove(device.getDeviceId());
        
        logger.info("Device removed from monitoring: " + device.getDeviceName());
    }
    
    @Override
    public void onDeviceStateChanged(SmartDevice device) {
        if (device == null) {
            logger.warn("Received null device state change notification");
            return;
        }
        
        try {
            String deviceId = device.getDeviceId();
            
            // Update statistics
            deviceStateChanges.merge(deviceId, 1, Integer::sum);
            lastUpdateTimes.put(deviceId, LocalDateTime.now());
            
            // Log the state change
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            logger.info(String.format("[%s] Device state changed: %s -> %s", 
                timestamp, device.getDeviceName(), device.isOn() ? "ON" : "OFF"));
            
            // Perform additional monitoring tasks
            checkDeviceHealth(device);
            
        } catch (Exception e) {
            logger.error("Error processing device state change for: " + device.getDeviceName(), e);
        }
    }
    
    private void checkDeviceHealth(SmartDevice device) {
        try {
            String deviceId = device.getDeviceId();
            int changeCount = deviceStateChanges.getOrDefault(deviceId, 0);
            
            // Alert if device has too many state changes (potential issue)
            if (changeCount > 50) {
                logger.warn("Device " + device.getDeviceName() + " has excessive state changes: " + changeCount);
            }
            
            // Log normal operation
            if (changeCount % 10 == 0 && changeCount > 0) {
                logger.info("Device " + device.getDeviceName() + " health check: " + changeCount + " state changes");
            }
            
        } catch (Exception e) {
            logger.error("Error during device health check", e);
        }
    }
    
    public void printStatistics() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEVICE MONITORING STATISTICS");
        System.out.println("=".repeat(50));
        
        if (deviceStateChanges.isEmpty()) {
            System.out.println("No devices are currently being monitored.");
            return;
        }
        
        deviceStateChanges.forEach((deviceId, changeCount) -> {
            LocalDateTime lastUpdate = lastUpdateTimes.get(deviceId);
            String formattedTime = lastUpdate != null ? 
                lastUpdate.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "Unknown";
            
            System.out.printf("Device ID: %-20s | Changes: %-3d | Last Update: %s%n",
                deviceId, changeCount, formattedTime);
        });
    }
    
    public int getTotalStateChanges() {
        return deviceStateChanges.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public int getDeviceStateChanges(String deviceId) {
        return deviceStateChanges.getOrDefault(deviceId, 0);
    }
}