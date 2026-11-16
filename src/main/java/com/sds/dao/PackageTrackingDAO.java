package com.sds.dao;

import com.sds.model.PackageTracking;
import com.sds.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageTrackingDAO {

    public boolean addTracking(int packageId, String status, String location, String remarks) {
        String sql = "INSERT INTO PackageTracking (package_id, status, location, remarks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            stmt.setString(2, status);
            stmt.setString(3, location);
            stmt.setString(4, remarks);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PackageTracking> getTrackingByPackageId(int packageId) {
        List<PackageTracking> trackingList = new ArrayList<>();
        String sql = "SELECT * FROM PackageTracking WHERE package_id = ? ORDER BY updated_at ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                trackingList.add(new PackageTracking(
                    rs.getInt("tracking_id"),
                    rs.getInt("package_id"),
                    rs.getString("status"),
                    rs.getString("location"),
                    rs.getString("remarks"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackingList;
    }
}
