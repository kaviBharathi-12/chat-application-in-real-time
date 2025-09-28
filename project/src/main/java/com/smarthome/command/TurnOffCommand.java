package com.smarthome.command;

import com.smarthome.devices.SmartDevice;

/**
 * COMMAND PATTERN (Behavioral)
 * Concrete command for turning off a device
 */
public class TurnOffCommand extends BaseCommand {
    
    public TurnOffCommand(SmartDevice device) {
        super(device);
    }
    
    @Override
    protected void executeCommand() {
        device.turnOff();
        System.out.println("✓ " + device.getDeviceName() + " has been turned OFF");
    }
    
    @Override
    public String getDescription() {
        return "Turn OFF " + device.getDeviceName();
    }
}