package com.smarthome.controller;

import com.smarthome.command.*;
import com.smarthome.devices.*;
import com.smarthome.exceptions.DeviceException;
import com.smarthome.exceptions.SystemException;
import com.smarthome.factory.DeviceFactory;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;
import com.smarthome.observer.DeviceStatusMonitor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SINGLETON PATTERN (Creational)
 * Central controller for the smart home system
 * Ensures only one instance manages the entire system
 */
public class HomeAutomationController {
    private static final Logger logger = LoggerFactory.getLogger(HomeAutomationController.class);
    private static volatile HomeAutomationController instance;
    private static final Object lock = new Object();
    
    private final Map<String, SmartDevice> devices;
    private final CommandInvoker commandInvoker;
    private final DeviceStatusMonitor statusMonitor;
    private final AtomicBoolean systemRunning;
    private final Scanner scanner;
    
    private HomeAutomationController() {
        this.devices = new HashMap<>();
        this.commandInvoker = new CommandInvoker();
        this.statusMonitor = new DeviceStatusMonitor();
        this.systemRunning = new AtomicBoolean(false);
        this.scanner = new Scanner(System.in);
        logger.info("Home Automation Controller initialized");
    }
    
    public static HomeAutomationController getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new HomeAutomationController();
                }
            }
        }
        return instance;
    }
    
    public void startSystem() throws SystemException {
        try {
            systemRunning.set(true);
            initializeDevices();
            showWelcomeMessage();
            runMainLoop();
        } catch (Exception e) {
            logger.error("Failed to start system", e);
            throw new SystemException("System startup failed: " + e.getMessage());
        }
    }
    
    private void initializeDevices() throws DeviceException {
        try {
            // Create various smart devices using Factory Pattern
            SmartDevice light = DeviceFactory.createDevice("LIGHT", "living-room-light", "Living Room Light");
            SmartDevice thermostat = DeviceFactory.createDevice("THERMOSTAT", "main-thermostat", "Main Thermostat");
            SmartDevice securityCamera = DeviceFactory.createDevice("CAMERA", "front-door-camera", "Front Door Camera");
            
            // Add devices and register with observer
            addDevice(light);
            addDevice(thermostat);
            addDevice(securityCamera);
            
            logger.info("All devices initialized successfully");
        } catch (Exception e) {
            throw new DeviceException("Failed to initialize devices: " + e.getMessage());
        }
    }
    
    private void addDevice(SmartDevice device) {
        devices.put(device.getDeviceId(), device);
        statusMonitor.addDevice(device); // Observer pattern
        logger.info("Device added: " + device.getDeviceName());
    }
    
    private void runMainLoop() {
        while (systemRunning.get()) {
            try {
                showMainMenu();
                int choice = getValidChoice();
                processChoice(choice);
            } catch (Exception e) {
                logger.error("Error in main loop", e);
                System.out.println("An error occurred. Please try again.");
            }
        }
        cleanup();
    }
    
    private void showWelcomeMessage() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   SMART HOME AUTOMATION SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("Welcome! Managing " + devices.size() + " devices");
    }
    
    private void showMainMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("MAIN MENU:");
        System.out.println("1. Control Device");
        System.out.println("2. View Device Status");
        System.out.println("3. Add New Device");
        System.out.println("4. Execute Batch Commands");
        System.out.println("5. View System Statistics");
        System.out.println("6. Exit System");
        System.out.print("Enter your choice (1-6): ");
    }
    
    private int getValidChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > 6) {
                System.out.println("Invalid choice. Please enter 1-6.");
                return getValidChoice();
            }
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return getValidChoice();
        }
    }
    
    private void processChoice(int choice) {
        switch (choice) {
            case 1 -> controlDevice();
            case 2 -> viewDeviceStatus();
            case 3 -> addNewDevice();
            case 4 -> executeBatchCommands();
            case 5 -> viewSystemStatistics();
            case 6 -> {
                systemRunning.set(false);
                System.out.println("Shutting down system...");
            }
        }
    }
    
    private void controlDevice() {
        if (devices.isEmpty()) {
            System.out.println("No devices available.");
            return;
        }
        
        System.out.println("\nAvailable Devices:");
        devices.values().forEach(device -> 
            System.out.println("- " + device.getDeviceId() + ": " + device.getDeviceName()));
        
        System.out.print("Enter device ID: ");
        String deviceId = scanner.nextLine().trim();
        
        SmartDevice device = devices.get(deviceId);
        if (device == null) {
            System.out.println("Device not found!");
            return;
        }
        
        showDeviceActions(device);
    }
    
    private void showDeviceActions(SmartDevice device) {
        System.out.println("\nActions for " + device.getDeviceName() + ":");
        System.out.println("1. Turn On");
        System.out.println("2. Turn Off");
        System.out.println("3. Get Status");
        System.out.print("Choose action (1-3): ");
        
        try {
            int action = Integer.parseInt(scanner.nextLine().trim());
            Command command = createCommand(device, action);
            if (command != null) {
                commandInvoker.executeCommand(command);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    private Command createCommand(SmartDevice device, int action) {
        return switch (action) {
            case 1 -> new TurnOnCommand(device);
            case 2 -> new TurnOffCommand(device);
            case 3 -> new GetStatusCommand(device);
            default -> {
                System.out.println("Invalid action.");
                yield null;
            }
        };
    }
    
    private void viewDeviceStatus() {
        if (devices.isEmpty()) {
            System.out.println("No devices available.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("DEVICE STATUS REPORT");
        System.out.println("=".repeat(40));
        
        devices.values().forEach(device -> {
            System.out.printf("%-20s | %-10s | %s%n", 
                device.getDeviceName(), 
                device.isOn() ? "ON" : "OFF",
                device.getStatus());
        });
    }
    
    private void addNewDevice() {
        System.out.println("\nDevice Types Available:");
        System.out.println("1. LIGHT");
        System.out.println("2. THERMOSTAT");
        System.out.println("3. CAMERA");
        System.out.print("Select device type (1-3): ");
        
        try {
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            String deviceType = switch (typeChoice) {
                case 1 -> "LIGHT";
                case 2 -> "THERMOSTAT";
                case 3 -> "CAMERA";
                default -> null;
            };
            
            if (deviceType == null) {
                System.out.println("Invalid device type.");
                return;
            }
            
            System.out.print("Enter device ID: ");
            String deviceId = scanner.nextLine().trim();
            
            if (devices.containsKey(deviceId)) {
                System.out.println("Device ID already exists!");
                return;
            }
            
            System.out.print("Enter device name: ");
            String deviceName = scanner.nextLine().trim();
            
            SmartDevice newDevice = DeviceFactory.createDevice(deviceType, deviceId, deviceName);
            addDevice(newDevice);
            System.out.println("Device added successfully!");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (DeviceException e) {
            System.out.println("Failed to create device: " + e.getMessage());
        }
    }
    
    private void executeBatchCommands() {
        System.out.println("\nBatch Operations:");
        System.out.println("1. Turn All Devices On");
        System.out.println("2. Turn All Devices Off");
        System.out.print("Choose operation (1-2): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            List<Command> batchCommands = new ArrayList<>();
            
            for (SmartDevice device : devices.values()) {
                Command command = switch (choice) {
                    case 1 -> new TurnOnCommand(device);
                    case 2 -> new TurnOffCommand(device);
                    default -> null;
                };
                if (command != null) {
                    batchCommands.add(command);
                }
            }
            
            commandInvoker.executeBatchCommands(batchCommands);
            System.out.println("Batch operation completed!");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    private void viewSystemStatistics() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("SYSTEM STATISTICS");
        System.out.println("=".repeat(40));
        System.out.println("Total Devices: " + devices.size());
        System.out.println("Online Devices: " + devices.values().stream().mapToLong(d -> d.isOn() ? 1 : 0).sum());
        System.out.println("Commands Executed: " + commandInvoker.getCommandHistory().size());
        System.out.println("System Uptime: Running");
        System.out.println("Memory Usage: Optimized");
    }
    
    private void cleanup() {
        try {
            scanner.close();
            logger.info("System shutdown completed");
            System.out.println("System shutdown completed. Goodbye!");
        } catch (Exception e) {
            logger.error("Error during cleanup", e);
        }
    }
}