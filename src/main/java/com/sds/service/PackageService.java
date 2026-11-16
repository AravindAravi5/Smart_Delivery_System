package com.sds.service;

import com.sds.dao.PackageDAO;
import com.sds.model.Package;

import java.util.List;
import java.util.Optional;

public class PackageService {
    private final PackageDAO packageDAO;

    public PackageService() {
        this.packageDAO = new PackageDAO();
    }

    public boolean addPackage(Package pkg) {
        if (pkg.getWeight() <= 0) {
            System.out.println("Invalid package weight.");
            return false;
        }
        return packageDAO.addPackage(pkg);
    }

    public Optional<Package> getPackage(int packageId) {
        return packageDAO.getPackageById(packageId);
    }

    public List<Package> getAllPackages() {
        return packageDAO.getAllPackages();
    }

    public List<Package> getPackagesByAgentId(int agentId) {
        return packageDAO.getPackagesByAgentId(agentId);
    }

    public boolean updatePackage(Package pkg) {
        return packageDAO.updatePackage(pkg);
    }

    public boolean deletePackage(int packageId) {
        return packageDAO.deletePackage(packageId);
    }
    
    // Day 20: Uses JDBC Transaction internally in DAO
    public boolean assignPackageToAgent(int packageId, int agentId, String location) {
        return packageDAO.assignPackageTransaction(packageId, agentId, location);
    }
}
