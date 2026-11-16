package com.sds.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/SmartDeliveryDB";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Updated to no password
    
    // Using Singleton pattern for Connection might be anti-pattern in multithreaded environments
    // But we will use a basic Connection Provider. In Day 37, connection pooling can be added.
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
