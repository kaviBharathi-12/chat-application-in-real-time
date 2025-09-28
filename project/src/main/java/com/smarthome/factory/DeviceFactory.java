package com.smarthome.factory;

import com.smarthome.adapter.LegacyDeviceAdapter;
import com.smarthome.decorator.SecurityDeviceDecorator;
import com.smarthome.decorator.SmartDeviceDecorator;
import com.smarthome.devices.*;
import com.smarthome.exceptions.DeviceException;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

/**
 * FACTORY PATTERN (Creational)
 * Creates different types of smart devices based on device type
 * Encapsulates object creation logic and promotes loose coupling
 */
public class DeviceFactory {
    private static final Logger logger = LoggerFactory.getLogger(DeviceFactory.class);
    
    private DeviceFactory() {
        // Private constructor to prevent instantiation
    }
    
    public static SmartDevice createDevice(String deviceType, String deviceId, String deviceName) throws DeviceException {
        if (deviceType == null || deviceType.trim().isEmpty()) {
            throw new DeviceException("Device type cannot be null or empty");
        }
        
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new DeviceException("Device ID cannot be null or empty");
        }
        
        if (deviceName == null || deviceName.trim().isEmpty()) {
            throw new DeviceException("Device name cannot be null or empty");
        }
        
        try {
            SmartDevice device = switch (deviceType.toUpperCase()) {
                case "LIGHT" -> createSmartLight(deviceId, deviceName);
                case "THERMOSTAT" -> createSmartThermostat(deviceId, deviceName);
                case "CAMERA" -> createSecurityCamera(deviceId, deviceName);
                case "LEGACY" -> createLegacyDevice(deviceId, deviceName);
                default -> throw new DeviceException("Unsupported device type: " + deviceType);
            };
            
            logger.info("Created device: " + deviceType + " - " + deviceName);
            return device;
            
        } catch (Exception e) {
            logger.error("Failed to create device: " + deviceType, e);
            throw new DeviceException("Device creation failed: " + e.getMessage());
        }
    }
    
    private static SmartDevice createSmartLight(String deviceId, String deviceName) {
        SmartLight light = new SmartLight(deviceId, deviceName);
        // Apply decorator pattern for additional features
        SmartDeviceDecorator decoratedLight = new SecurityDeviceDecorator(light);
        logger.debug("Smart light created with security features: " + deviceName);
        return decoratedLight;
    }
    
    private static SmartDevice createSmartThermostat(String deviceId, String deviceName) {
        SmartThermostat thermostat = new SmartThermostat(deviceId, deviceName);
        logger.debug("Smart thermostat created: " + deviceName);
        return thermostat;
    }
    
    private static SmartDevice createSecurityCamera(String deviceId, String deviceName) {
        SecurityCamera camera = new SecurityCamera(deviceId, deviceName);
        // Apply decorator for enhanced security features
        SmartDeviceDecorator decoratedCamera = new SecurityDeviceDecorator(camera);
        logger.debug("Security camera created with enhanced features: " + deviceName);
        return decoratedCamera;
    }
    
    private static SmartDevice createLegacyDevice(String deviceId, String deviceName) {
        // Create legacy device using adapter pattern
        LegacyDevice legacyDevice = new LegacyDevice(deviceId, deviceName);
        LegacyDeviceAdapter adapter = new LegacyDeviceAdapter(legacyDevice);
        logger.debug("Legacy device created with adapter: " + deviceName);
        return adapter;
    }
}