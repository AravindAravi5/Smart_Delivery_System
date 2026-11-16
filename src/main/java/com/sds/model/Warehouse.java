package com.sds.model;

public class Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String city;
    private int capacity;
    private int availableSpace;
    private String manager;
    private String contact;

    public Warehouse() {}

    public Warehouse(int warehouseId, String warehouseName, String city, int capacity, int availableSpace, String manager, String contact) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.city = city;
        this.capacity = capacity;
        this.availableSpace = availableSpace;
        this.manager = manager;
        this.contact = contact;
    }

    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getAvailableSpace() { return availableSpace; }
    public void setAvailableSpace(int availableSpace) { this.availableSpace = availableSpace; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + warehouseId +
                ", name='" + warehouseName + '\'' +
                ", city='" + city + '\'' +
                ", space=" + availableSpace + "/" + capacity +
                '}';
    }
}
