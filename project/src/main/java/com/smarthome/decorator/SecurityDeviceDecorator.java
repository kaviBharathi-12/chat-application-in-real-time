package com.smarthome.decorator;

import com.smarthome.devices.SmartDevice;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DECORATOR PATTERN (Structural)
 * Concrete decorator that adds security features to any smart device
 * Demonstrates how decorators can enhance functionality without modifying original objects
 */
public class SecurityDeviceDecorator extends SmartDeviceDecorator {
    private static final Logger logger = LoggerFactory.getLogger(SecurityDeviceDecorator.class);
    
    private boolean motionDetectionEnabled;
    private boolean alertsEnabled;
    private LocalDateTime lastMotionDetected;
    private int alertCount;
    
    public SecurityDeviceDecorator(SmartDevice device) {
        super(device);
        this.motionDetectionEnabled = true;
        this.alertsEnabled = true;
        this.lastMotionDetected = null;
        this.alertCount = 0;
        logger.info("Security features added to device: " + device.getDeviceName());
    }
    
    @Override
    public String getDeviceName() {
        return decoratedDevice.getDeviceName() + " [Secured]";
    }
    
    @Override
    public void turnOn() {
        // Add security check before turning on
        performSecurityCheck();
        decoratedDevice.turnOn();
        
        if (decoratedDevice.isOn()) {
            activateSecurityFeatures();
        }
    }
    
    @Override
    public void turnOff() {
        // Log security event before turning off
        logSecurityEvent("Device turning off");
        decoratedDevice.turnOff();
        
        if (!decoratedDevice.isOn()) {
            deactivateSecurityFeatures();
        }
    }
    
    @Override
    public String getStatus() {
        String baseStatus = decoratedDevice.getStatus();
        String securityStatus = getSecurityStatus();
        return String.format("%s | Security: %s", baseStatus, securityStatus);
    }
    
    private void performSecurityCheck() {
        logger.debug("Performing security check for device: " + getDeviceName());
        
        // Simulate security validation
        if (alertsEnabled && motionDetectionEnabled) {
            logger.info("Security check passed for: " + getDeviceName());
        }
    }
    
    private void activateSecurityFeatures() {
        if (motionDetectionEnabled) {
            logger.info("Motion detection activated for: " + getDeviceName());
        }
        
        if (alertsEnabled) {
            logger.info("Security alerts enabled for: " + getDeviceName());
        }
        
        // Simulate motion detection
        simulateMotionDetection();
    }
    
    private void deactivateSecurityFeatures() {
        logger.info("Security features deactivated for: " + getDeviceName());
    }
    
    private void simulateMotionDetection() {
        // Simulate random motion detection
        if (Math.random() < 0.3) { // 30% chance of motion detection
            detectMotion();
        }
    }
    
    public void detectMotion() {
        lastMotionDetected = LocalDateTime.now();
        alertCount++;
        
        String timestamp = lastMotionDetected.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logger.info("🚨 Motion detected by " + getDeviceName() + " at " + timestamp);
        
        if (alertsEnabled) {
            sendSecurityAlert("Motion detected");
        }
        
        // Notify observers about security event
        notifyObservers();
    }
    
    private void sendSecurityAlert(String alertMessage) {
        logger.warn("SECURITY ALERT from " + getDeviceName() + ": " + alertMessage);
        System.out.println("🔔 SECURITY ALERT: " + alertMessage + " on " + getDeviceName());
    }
    
    private void logSecurityEvent(String event) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logger.info("Security Event [" + timestamp + "]: " + event + " - " + getDeviceName());
    }
    
    private String getSecurityStatus() {
        StringBuilder status = new StringBuilder();
        
        status.append("Motion Detection: ").append(motionDetectionEnabled ? "ON" : "OFF");
        status.append(" | Alerts: ").append(alertsEnabled ? "ON" : "OFF");
        status.append(" | Alert Count: ").append(alertCount);
        
        if (lastMotionDetected != null) {
            String lastMotion = lastMotionDetected.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            status.append(" | Last Motion: ").append(lastMotion);
        }
        
        return status.toString();
    }
    
    // Security-specific methods
    public void enableMotionDetection() {
        motionDetectionEnabled = true;
        logger.info("Motion detection enabled for: " + getDeviceName());
    }
    
    public void disableMotionDetection() {
        motionDetectionEnabled = false;
        logger.info("Motion detection disabled for: " + getDeviceName());
    }
    
    public void enableAlerts() {
        alertsEnabled = true;
        logger.info("Security alerts enabled for: " + getDeviceName());
    }
    
    public void disableAlerts() {
        alertsEnabled = false;
        logger.info("Security alerts disabled for: " + getDeviceName());
    }
    
    public boolean isMotionDetectionEnabled() {
        return motionDetectionEnabled;
    }
    
    public boolean isAlertsEnabled() {
        return alertsEnabled;
    }
    
    public LocalDateTime getLastMotionDetected() {
        return lastMotionDetected;
    }
    
    public int getAlertCount() {
        return alertCount;
    }
    
    public void resetAlertCount() {
        alertCount = 0;
        logger.info("Alert count reset for: " + getDeviceName());
    }
}