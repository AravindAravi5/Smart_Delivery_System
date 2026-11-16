package com.sds.service;

import com.sds.dao.DeliveryAgentDAO;
import com.sds.model.DeliveryAgent;

import java.util.List;
import java.util.Optional;

public class DeliveryAgentService {
    private final DeliveryAgentDAO agentDAO;

    public DeliveryAgentService() {
        this.agentDAO = new DeliveryAgentDAO();
    }

    public boolean addAgent(DeliveryAgent agent) {
        if (agent.getPhone() == null || agent.getPhone().length() < 10) {
            System.out.println("Invalid phone number.");
            return false;
        }
        return agentDAO.addAgent(agent);
    }

    public Optional<DeliveryAgent> getAgent(int agentId) {
        return agentDAO.getAgentById(agentId);
    }

    public Optional<DeliveryAgent> getAgentByUserId(int userId) {
        return agentDAO.getAgentByUserId(userId);
    }

    public List<DeliveryAgent> getAllAgents() {
        return agentDAO.getAllAgents();
    }
    
    public List<DeliveryAgent> getAvailableAgents() {
        return agentDAO.getAgentsByStatus("AVAILABLE");
    }

    public boolean updateAgent(DeliveryAgent agent) {
        return agentDAO.updateAgent(agent);
    }

    public boolean deleteAgent(int agentId) {
        return agentDAO.deleteAgent(agentId);
    }
}
