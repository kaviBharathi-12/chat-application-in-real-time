package com.smarthome.decorator;

import com.smarthome.devices.SmartDevice;
import com.smarthome.observer.DeviceObserver;

import java.util.List;

/**
 * DECORATOR PATTERN (Structural)
 * Base decorator class for adding additional features to smart devices
 * Allows behavior to be added to devices dynamically without altering their structure
 */
public abstract class SmartDeviceDecorator implements SmartDevice {
    protected final SmartDevice decoratedDevice;
    
    protected SmartDeviceDecorator(SmartDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("Device to decorate cannot be null");
        }
        this.decoratedDevice = device;
    }
    
    @Override
    public String getDeviceId() {
        return decoratedDevice.getDeviceId();
    }
    
    @Override
    public String getDeviceName() {
        return decoratedDevice.getDeviceName();
    }
    
    @Override
    public boolean isOn() {
        return decoratedDevice.isOn();
    }
    
    @Override
    public void turnOn() {
        decoratedDevice.turnOn();
    }
    
    @Override
    public void turnOff() {
        decoratedDevice.turnOff();
    }
    
    @Override
    public String getStatus() {
        return decoratedDevice.getStatus();
    }
    
    @Override
    public void addObserver(DeviceObserver observer) {
        decoratedDevice.addObserver(observer);
    }
    
    @Override
    public void removeObserver(DeviceObserver observer) {
        decoratedDevice.removeObserver(observer);
    }
    
    @Override
    public void notifyObservers() {
        decoratedDevice.notifyObservers();
    }
    
    @Override
    public List<DeviceObserver> getObservers() {
        return decoratedDevice.getObservers();
    }
    
    protected SmartDevice getDecoratedDevice() {
        return decoratedDevice;
    }
}