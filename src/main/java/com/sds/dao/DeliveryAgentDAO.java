package com.sds.dao;

import com.sds.model.DeliveryAgent;
import com.sds.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliveryAgentDAO {

    public boolean addAgent(DeliveryAgent agent) {
        String sql = "INSERT INTO DeliveryAgents (user_id, full_name, phone, email, vehicle_type, license_number, city, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setObject(1, agent.getUserId() == 0 ? null : agent.getUserId(), Types.INTEGER);
            stmt.setString(2, agent.getFullName());
            stmt.setString(3, agent.getPhone());
            stmt.setString(4, agent.getEmail());
            stmt.setString(5, agent.getVehicleType());
            stmt.setString(6, agent.getLicenseNumber());
            stmt.setString(7, agent.getCity());
            stmt.setString(8, agent.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    agent.setAgentId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<DeliveryAgent> getAgentById(int agentId) {
        String sql = "SELECT * FROM DeliveryAgents WHERE agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, agentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToAgent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<DeliveryAgent> getAgentByUserId(int userId) {
        String sql = "SELECT * FROM DeliveryAgents WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToAgent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<DeliveryAgent> getAllAgents() {
        List<DeliveryAgent> agents = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryAgents";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                agents.add(mapResultSetToAgent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    public List<DeliveryAgent> getAgentsByStatus(String status) {
        List<DeliveryAgent> agents = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryAgents WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                agents.add(mapResultSetToAgent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    public boolean updateAgent(DeliveryAgent agent) {
        String sql = "UPDATE DeliveryAgents SET full_name = ?, phone = ?, email = ?, vehicle_type = ?, license_number = ?, city = ?, status = ? WHERE agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, agent.getFullName());
            stmt.setString(2, agent.getPhone());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getVehicleType());
            stmt.setString(5, agent.getLicenseNumber());
            stmt.setString(6, agent.getCity());
            stmt.setString(7, agent.getStatus());
            stmt.setInt(8, agent.getAgentId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAgent(int agentId) {
        String sql = "DELETE FROM DeliveryAgents WHERE agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, agentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private DeliveryAgent mapResultSetToAgent(ResultSet rs) throws SQLException {
        Integer userId = (Integer) rs.getObject("user_id");
        return new DeliveryAgent(
                rs.getInt("agent_id"),
                userId != null ? userId : 0,
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("vehicle_type"),
                rs.getString("license_number"),
                rs.getString("city"),
                rs.getString("status"),
                rs.getTimestamp("joined_at")
        );
    }
}
