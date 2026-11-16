package com.sds.service;

import com.sds.model.Package;
import com.sds.model.DeliveryAgent;

import java.util.LinkedList;
import java.util.Queue;

public class DispatchService {

    // DSA: Queue for Dispatch Packages
    private Queue<Package> dispatchQueue = new LinkedList<>();
    private final PackageService packageService;
    private final DeliveryAgentService agentService;
    private final NotificationService notificationService;

    public DispatchService(PackageService packageService, DeliveryAgentService agentService, NotificationService notificationService) {
        this.packageService = packageService;
        this.agentService = agentService;
        this.notificationService = notificationService;
    }

    public void addToDispatchQueue(Package pkg) {
        dispatchQueue.add(pkg);
        System.out.println("Package " + pkg.getPackageId() + " added to dispatch queue.");
    }

    public void processDispatch() {
        while (!dispatchQueue.isEmpty()) {
            Package pkg = dispatchQueue.poll();
            
            // Assign to next available agent
            var agents = agentService.getAvailableAgents();
            if (!agents.isEmpty()) {
                DeliveryAgent agent = agents.get(0);
                if (agent != null) {
                    boolean success = packageService.assignPackageToAgent(pkg.getPackageId(), agent.getAgentId(), pkg.getSourceCity());
                    if (success) {
                        System.out.println("Assigned Package " + pkg.getTrackingId() + " to Agent " + agent.getFullName());
                        notificationService.sendNotification(pkg.getCustomerId(), "Your package has been assigned to " + agent.getFullName() + " and dispatched!");
                    } else {
                        System.out.println("Failed to dispatch package " + pkg.getPackageId());
                    }
                }
            } else {
                System.out.println("No available agents for dispatching package " + pkg.getPackageId());
                // Re-add to queue for later
                dispatchQueue.add(pkg);
                break;
            }
        }
    }
}
