package com.sds.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// Day 35: Logging Framework
public class LoggerUtil {
    
    private static final Logger logger = Logger.getLogger("SmartDeliverySystem");

    static {
        try {
            FileHandler fh = new FileHandler("app.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger.");
        }
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void error(String message, Throwable t) {
        logger.log(Level.SEVERE, message, t);
    }
}
