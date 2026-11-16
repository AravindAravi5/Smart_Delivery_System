package com.sds.service;

public class NotificationService {

    public void sendNotification(int customerId, String message) {
        // In Day 27, this is a console version
        System.out.println("\n[NOTIFICATION - Customer ID: " + customerId + "]");
        System.out.println("Message: " + message);
        System.out.println("----------------------------------------\n");
        
        // Future scope: integrate with Email, SMS, WhatsApp APIs
    }
}
