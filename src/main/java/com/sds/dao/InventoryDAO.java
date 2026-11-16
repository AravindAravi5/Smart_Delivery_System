package com.sds.dao;

import com.sds.model.Inventory;
import com.sds.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public boolean storePackage(int warehouseId, int packageId, String rackNumber, String section) {
        String sql = "INSERT INTO Inventory (warehouse_id, package_id, rack_number, section) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, warehouseId);
            stmt.setInt(2, packageId);
            stmt.setString(3, rackNumber);
            stmt.setString(4, section);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removePackage(int packageId) {
        String sql = "UPDATE Inventory SET exit_date = CURRENT_TIMESTAMP WHERE package_id = ? AND exit_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Inventory> getInventoryByWarehouse(int warehouseId) {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM Inventory WHERE warehouse_id = ? AND exit_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                inventoryList.add(new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("warehouse_id"),
                    rs.getInt("package_id"),
                    rs.getString("rack_number"),
                    rs.getString("section"),
                    rs.getTimestamp("entry_date"),
                    rs.getTimestamp("exit_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
}
