package com.smarthome.observer;

import com.smarthome.devices.SmartDevice;

/**
 * OBSERVER PATTERN (Behavioral)
 * Interface for observers that want to be notified of device state changes
 */
public interface DeviceObserver {
    void onDeviceStateChanged(SmartDevice device);
}