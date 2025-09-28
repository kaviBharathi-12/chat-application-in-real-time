package com.smarthome.devices;

import com.smarthome.observer.DeviceObserver;

import java.util.List;

/**
 * Base interface for all smart devices in the system
 * Defines common operations that all smart devices must support
 */
public interface SmartDevice {
    String getDeviceId();
    String getDeviceName();
    boolean isOn();
    void turnOn();
    void turnOff();
    String getStatus();
    void addObserver(DeviceObserver observer);
    void removeObserver(DeviceObserver observer);
    void notifyObservers();
    List<DeviceObserver> getObservers();
}