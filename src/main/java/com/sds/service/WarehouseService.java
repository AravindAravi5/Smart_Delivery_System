package com.sds.service;

import com.sds.dao.WarehouseDAO;
import com.sds.model.Warehouse;

import java.util.List;
import java.util.Optional;

public class WarehouseService {
    private final WarehouseDAO warehouseDAO;

    public WarehouseService() {
        this.warehouseDAO = new WarehouseDAO();
    }

    public boolean addWarehouse(Warehouse warehouse) {
        if (warehouse.getCapacity() <= 0) {
            System.out.println("Invalid warehouse capacity.");
            return false;
        }
        warehouse.setAvailableSpace(warehouse.getCapacity()); // initially full space available
        return warehouseDAO.addWarehouse(warehouse);
    }

    public Optional<Warehouse> getWarehouse(int warehouseId) {
        return warehouseDAO.getWarehouseById(warehouseId);
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.getAllWarehouses();
    }
}
