package com.sds.service;

import com.sds.dao.InventoryDAO;
import com.sds.dao.WarehouseDAO;
import com.sds.model.Package;
import com.sds.model.Warehouse;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class InventoryService {
    
    private final InventoryDAO inventoryDAO;
    private final WarehouseDAO warehouseDAO;
    private final RouteService routeService;
    
    private Queue<Package> processingQueue = new LinkedList<>();

    public InventoryService(RouteService routeService) {
        this.inventoryDAO = new InventoryDAO();
        this.warehouseDAO = new WarehouseDAO();
        this.routeService = routeService;
    }

    public boolean storePackageInWarehouse(Warehouse warehouse, Package pkg, String rack, String section) {
        if (warehouse.getAvailableSpace() > 0) {
            boolean stored = inventoryDAO.storePackage(warehouse.getWarehouseId(), pkg.getPackageId(), rack, section);
            if (stored) {
                warehouseDAO.updateWarehouseSpace(warehouse.getWarehouseId(), -1);
                processingQueue.add(pkg);
                return true;
            }
        }
        return false;
    }

    public boolean removePackageFromWarehouse(int warehouseId, int packageId) {
        boolean removed = inventoryDAO.removePackage(packageId);
        if (removed) {
            warehouseDAO.updateWarehouseSpace(warehouseId, 1);
            return true;
        }
        return false;
    }

    public Warehouse allocateBestWarehouse(Package pkg) {
        List<Warehouse> allWarehouses = warehouseDAO.getAllWarehouses();
        String packageCity = pkg.getSourceCity();
        
        if (!routeService.cityExists(packageCity) || allWarehouses.isEmpty()) return null;

        PriorityQueue<WarehouseDistance> pq = new PriorityQueue<>((w1, w2) -> Integer.compare(w1.distance, w2.distance));

        for (Warehouse w : allWarehouses) {
            if (w.getAvailableSpace() > 0) {
                String wCity = w.getCity();
                if (routeService.cityExists(wCity)) {
                    if (wCity.equals(packageCity)) {
                        pq.add(new WarehouseDistance(w, 0));
                    } else {
                        RouteService.RouteResult res = routeService.findShortestPathDijkstra(packageCity, wCity);
                        if (res != null) {
                            pq.add(new WarehouseDistance(w, res.totalDistance));
                        }
                    }
                }
            }
        }

        if (!pq.isEmpty()) {
            return pq.poll().warehouse;
        }
        return null;
    }

    private static class WarehouseDistance {
        Warehouse warehouse;
        int distance;
        public WarehouseDistance(Warehouse warehouse, int distance) {
            this.warehouse = warehouse;
            this.distance = distance;
        }
    }
}
