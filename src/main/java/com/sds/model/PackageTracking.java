package com.sds.model;

import java.sql.Timestamp;

public class PackageTracking {
    private int trackingId;
    private int packageId;
    private String status;
    private String location;
    private String remarks;
    private Timestamp updatedAt;

    public PackageTracking(int trackingId, int packageId, String status, String location, String remarks, Timestamp updatedAt) {
        this.trackingId = trackingId;
        this.packageId = packageId;
        this.status = status;
        this.location = location;
        this.remarks = remarks;
        this.updatedAt = updatedAt;
    }

    public int getTrackingId() { return trackingId; }
    public int getPackageId() { return packageId; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getRemarks() { return remarks; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "[" + updatedAt + "] " + status + " at " + location + (remarks != null ? " (" + remarks + ")" : "");
    }
}
