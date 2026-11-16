package com.sds.model;

import java.sql.Timestamp;

public class DeliveryAgent {
    private int agentId;
    private int userId;
    private String fullName;
    private String phone;
    private String email;
    private String vehicleType;
    private String licenseNumber;
    private String city;
    private String status;
    private Timestamp joinedAt;

    public DeliveryAgent() {}

    public DeliveryAgent(int agentId, int userId, String fullName, String phone, String email, String vehicleType, String licenseNumber, String city, String status, Timestamp joinedAt) {
        this.agentId = agentId;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.city = city;
        this.status = status;
        this.joinedAt = joinedAt;
    }

    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }

    @Override
    public String toString() {
        return "DeliveryAgent{" +
                "id=" + agentId +
                ", name='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", vehicle='" + vehicleType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
