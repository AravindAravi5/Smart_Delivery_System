package com.sds.dao;

import com.sds.model.Package;
import com.sds.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackageDAO {

    public boolean addPackage(Package pkg) {
        String sql = "INSERT INTO Packages (tracking_id, customer_id, description, weight, source_city, destination_city, status, delivery_cost, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setPackagePreparedStatement(stmt, pkg);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    pkg.setPackageId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Package> getPackageById(int packageId) {
        String sql = "SELECT * FROM Packages WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToPackage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        String sql = "SELECT * FROM Packages";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                packages.add(mapResultSetToPackage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public List<Package> getPackagesByAgentId(int agentId) {
        List<Package> packages = new ArrayList<>();
        String sql = "SELECT * FROM Packages WHERE agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, agentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                packages.add(mapResultSetToPackage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public boolean updatePackage(Package pkg) {
        String sql = "UPDATE Packages SET tracking_id = ?, customer_id = ?, description = ?, weight = ?, source_city = ?, destination_city = ?, status = ?, delivery_cost = ?, agent_id = ? WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setPackagePreparedStatement(stmt, pkg);
            stmt.setInt(10, pkg.getPackageId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean assignPackageTransaction(int packageId, int agentId, String startLocation) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Update Package Agent & Status
            String updatePackageSql = "UPDATE Packages SET agent_id = ?, status = 'ASSIGNED' WHERE package_id = ?";
            try (PreparedStatement pkgStmt = conn.prepareStatement(updatePackageSql)) {
                pkgStmt.setInt(1, agentId);
                pkgStmt.setInt(2, packageId);
                pkgStmt.executeUpdate();
            }

            // 2. Update Delivery Agent Status
            String updateAgentSql = "UPDATE DeliveryAgents SET status = 'BUSY' WHERE agent_id = ?";
            try (PreparedStatement agentStmt = conn.prepareStatement(updateAgentSql)) {
                agentStmt.setInt(1, agentId);
                agentStmt.executeUpdate();
            }

            // 3. Insert Tracking History
            String insertTrackingSql = "INSERT INTO PackageTracking (package_id, status, location, remarks) VALUES (?, 'ASSIGNED', ?, 'Package assigned to agent')";
            try (PreparedStatement trackStmt = conn.prepareStatement(insertTrackingSql)) {
                trackStmt.setInt(1, packageId);
                trackStmt.setString(2, startLocation);
                trackStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean deletePackage(int packageId) {
        String sql = "DELETE FROM Packages WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setPackagePreparedStatement(PreparedStatement stmt, Package pkg) throws SQLException {
        stmt.setString(1, pkg.getTrackingId());
        stmt.setInt(2, pkg.getCustomerId());
        stmt.setString(3, pkg.getDescription());
        stmt.setDouble(4, pkg.getWeight());
        stmt.setString(5, pkg.getSourceCity());
        stmt.setString(6, pkg.getDestinationCity());
        stmt.setString(7, pkg.getStatus() != null ? pkg.getStatus() : "PENDING");
        stmt.setObject(8, pkg.getDeliveryCost(), Types.DECIMAL);
        stmt.setObject(9, pkg.getAgentId(), Types.INTEGER);
    }

    private Package mapResultSetToPackage(ResultSet rs) throws SQLException {
        Object agentObj = rs.getObject("agent_id");
        Integer agentId = agentObj != null ? ((Number) agentObj).intValue() : null;
        
        Object costObj = rs.getObject("delivery_cost");
        Double deliveryCost = costObj != null ? ((Number) costObj).doubleValue() : null;
        return new Package(
                rs.getInt("package_id"),
                rs.getString("tracking_id"),
                rs.getInt("customer_id"),
                rs.getString("description"),
                rs.getDouble("weight"),
                rs.getString("source_city"),
                rs.getString("destination_city"),
                rs.getString("status"),
                deliveryCost,
                agentId,
                rs.getTimestamp("created_at")
        );
    }
}
