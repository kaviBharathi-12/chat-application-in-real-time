package com.smarthome.command;

import com.smarthome.devices.SmartDevice;

/**
 * COMMAND PATTERN (Behavioral)
 * Concrete command for turning on a device
 */
public class TurnOnCommand extends BaseCommand {
    
    public TurnOnCommand(SmartDevice device) {
        super(device);
    }
    
    @Override
    protected void executeCommand() {
        device.turnOn();
        System.out.println("✓ " + device.getDeviceName() + " has been turned ON");
    }
    
    @Override
    public String getDescription() {
        return "Turn ON " + device.getDeviceName();
    }
}