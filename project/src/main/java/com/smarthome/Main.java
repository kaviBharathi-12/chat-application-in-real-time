package com.smarthome;

import com.smarthome.controller.HomeAutomationController;
import com.smarthome.exceptions.SystemException;
import com.smarthome.logger.Logger;
import com.smarthome.logger.LoggerFactory;

/**
 * Main entry point for Smart Home Automation System
 * Demonstrates various design patterns in a cohesive application
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Starting Smart Home Automation System...");
            
            HomeAutomationController controller = HomeAutomationController.getInstance();
            controller.startSystem();
            
        } catch (SystemException e) {
            logger.error("System failed to start: " + e.getMessage(), e);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Unexpected error occurred: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}