package com.sds.model;

import java.sql.Timestamp;

public class Inventory {
    private int inventoryId;
    private int warehouseId;
    private int packageId;
    private String rackNumber;
    private String section;
    private Timestamp entryDate;
    private Timestamp exitDate;

    public Inventory(int inventoryId, int warehouseId, int packageId, String rackNumber, String section, Timestamp entryDate, Timestamp exitDate) {
        this.inventoryId = inventoryId;
        this.warehouseId = warehouseId;
        this.packageId = packageId;
        this.rackNumber = rackNumber;
        this.section = section;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
    }

    public int getInventoryId() { return inventoryId; }
    public int getWarehouseId() { return warehouseId; }
    public int getPackageId() { return packageId; }
    public String getRackNumber() { return rackNumber; }
    public String getSection() { return section; }
    public Timestamp getEntryDate() { return entryDate; }
    public Timestamp getExitDate() { return exitDate; }

    @Override
    public String toString() {
        return "Inventory{" +
                "warehouseId=" + warehouseId +
                ", packageId=" + packageId +
                ", rack='" + rackNumber + '\'' +
                ", section='" + section + '\'' +
                '}';
    }
}
