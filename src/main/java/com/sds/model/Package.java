package com.sds.model;

import java.sql.Timestamp;

public class Package {
    private int packageId;
    private String trackingId;
    private int customerId;
    private String description;
    private double weight;
    private String sourceCity;
    private String destinationCity;
    private String status;
    private Double deliveryCost;
    private Integer agentId;
    private Timestamp createdAt;

    public Package() {}

    public Package(int packageId, String trackingId, int customerId, String description, double weight, String sourceCity, String destinationCity, String status, Double deliveryCost, Integer agentId, Timestamp createdAt) {
        this.packageId = packageId;
        this.trackingId = trackingId;
        this.customerId = customerId;
        this.description = description;
        this.weight = weight;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.status = status;
        this.deliveryCost = deliveryCost;
        this.agentId = agentId;
        this.createdAt = createdAt;
    }

    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getSourceCity() { return sourceCity; }
    public void setSourceCity(String sourceCity) { this.sourceCity = sourceCity; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getDeliveryCost() { return deliveryCost; }
    public void setDeliveryCost(Double deliveryCost) { this.deliveryCost = deliveryCost; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Package{" +
                "trackingId='" + trackingId + '\'' +
                ", from='" + sourceCity + '\'' +
                ", to='" + destinationCity + '\'' +
                ", weight=" + weight +
                ", status='" + status + '\'' +
                '}';
    }
}
