package com.sds.model;

import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private int userId;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String pincode;
    private Timestamp createdAt;

    public Customer() {}

    public Customer(int customerId, int userId, String fullName, String phone, String email, String address, String city, String pincode, Timestamp createdAt) {
        this.customerId = customerId;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.createdAt = createdAt;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + customerId +
                ", name='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
