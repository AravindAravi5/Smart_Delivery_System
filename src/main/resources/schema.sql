-- Smart Route & Delivery Management System - Database Schema
CREATE DATABASE IF NOT EXISTS SmartDeliveryDB;
USE SmartDeliveryDB;
SET FOREIGN_KEY_CHECKS = 0;
-- Drop tables if they exist to allow clean setup (reverse order of dependencies)
DROP TABLE IF EXISTS Inventory;
DROP TABLE IF EXISTS PackageTracking;
DROP TABLE IF EXISTS Packages;
DROP TABLE IF EXISTS Warehouses;
DROP TABLE IF EXISTS DeliveryAgents;
DROP TABLE IF EXISTS Routes;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Users;

-- ==========================================
-- PHASE 1: User's Provided Schema
-- ==========================================

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    -- Added user_id to link login to customer profile
    user_id INT UNIQUE, 
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    pincode VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Packages (
    package_id INT AUTO_INCREMENT PRIMARY KEY,
    tracking_id VARCHAR(20) UNIQUE NOT NULL,
    customer_id INT NOT NULL,
    description VARCHAR(255),
    weight DECIMAL(5,2),
    source_city VARCHAR(50),
    destination_city VARCHAR(50),
    status VARCHAR(20),
    -- Added cost and agent for Phase 2 compatibility
    delivery_cost DECIMAL(10, 2),
    agent_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(customer_id) REFERENCES Customers(customer_id)
);

CREATE TABLE DeliveryAgents (
    agent_id INT AUTO_INCREMENT PRIMARY KEY,
    -- Added user_id to link login to agent profile
    user_id INT UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100),
    vehicle_type VARCHAR(30),
    license_number VARCHAR(50) UNIQUE,
    city VARCHAR(50),
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Adding foreign key to Packages for agent_id
ALTER TABLE Packages ADD FOREIGN KEY (agent_id) REFERENCES DeliveryAgents(agent_id) ON DELETE SET NULL;

CREATE TABLE Routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    source_city VARCHAR(50),
    destination_city VARCHAR(50),
    distance INT
);

CREATE TABLE PackageTracking (
    tracking_id INT AUTO_INCREMENT PRIMARY KEY,
    package_id INT,
    status VARCHAR(50),
    location VARCHAR(100),
    remarks VARCHAR(200),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(package_id) REFERENCES Packages(package_id) ON DELETE CASCADE
);

-- ==========================================
-- PHASE 2: Advanced System Tables
-- ==========================================

CREATE TABLE Warehouses (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    available_space INT NOT NULL,
    manager VARCHAR(100),
    contact VARCHAR(20)
);

CREATE TABLE Inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    package_id INT UNIQUE NOT NULL,
    rack_number VARCHAR(20),
    section VARCHAR(20),
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exit_date TIMESTAMP NULL,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES Packages(package_id) ON DELETE CASCADE
);

-- ==========================================
-- Sample Data Insertion
-- ==========================================

-- Insert sample routes (bidirectional)
INSERT INTO Routes (source_city, destination_city, distance) VALUES 
('Hyderabad', 'Bangalore', 570), ('Bangalore', 'Hyderabad', 570),
('Hyderabad', 'Chennai', 630), ('Chennai', 'Hyderabad', 630),
('Bangalore', 'Chennai', 350), ('Chennai', 'Bangalore', 350),
('Bangalore', 'Mumbai', 980), ('Mumbai', 'Bangalore', 980),
('Mumbai', 'Pune', 150), ('Pune', 'Mumbai', 150),
('Chennai', 'Pune', 1100), ('Pune', 'Chennai', 1100);
