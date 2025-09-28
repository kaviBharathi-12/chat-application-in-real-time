package com.smarthome.command;

import com.smarthome.devices.SmartDevice;

/**
 * COMMAND PATTERN (Behavioral)
 * Concrete command for getting device status
 */
public class GetStatusCommand extends BaseCommand {
    
    public GetStatusCommand(SmartDevice device) {
        super(device);
    }
    
    @Override
    protected void executeCommand() {
        String status = device.getStatus();
        System.out.println("📊 " + device.getDeviceName() + " Status: " + status);
    }
    
    @Override
    public String getDescription() {
        return "Get Status of " + device.getDeviceName();
    }
}