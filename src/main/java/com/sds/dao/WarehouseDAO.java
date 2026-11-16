package com.sds.dao;

import com.sds.model.Warehouse;
import com.sds.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarehouseDAO {

    public boolean addWarehouse(Warehouse warehouse) {
        String sql = "INSERT INTO Warehouses (warehouse_name, city, capacity, available_space, manager, contact) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setString(2, warehouse.getCity());
            stmt.setInt(3, warehouse.getCapacity());
            stmt.setInt(4, warehouse.getAvailableSpace());
            stmt.setString(5, warehouse.getManager());
            stmt.setString(6, warehouse.getContact());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    warehouse.setWarehouseId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Warehouse> getWarehouseById(int warehouseId) {
        String sql = "SELECT * FROM Warehouses WHERE warehouse_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToWarehouse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM Warehouses";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                warehouses.add(mapResultSetToWarehouse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses;
    }

    public boolean updateWarehouseSpace(int warehouseId, int delta) {
        String sql = "UPDATE Warehouses SET available_space = available_space + ? WHERE warehouse_id = ? AND available_space + ? >= 0 AND available_space + ? <= capacity";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, delta);
            stmt.setInt(2, warehouseId);
            stmt.setInt(3, delta);
            stmt.setInt(4, delta);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Warehouse mapResultSetToWarehouse(ResultSet rs) throws SQLException {
        return new Warehouse(
            rs.getInt("warehouse_id"),
            rs.getString("warehouse_name"),
            rs.getString("city"),
            rs.getInt("capacity"),
            rs.getInt("available_space"),
            rs.getString("manager"),
            rs.getString("contact")
        );
    }
}
