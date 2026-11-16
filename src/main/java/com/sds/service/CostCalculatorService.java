package com.sds.service;


public class CostCalculatorService {
    
    private static final double BASE_COST = 50.0;
    private static final double COST_PER_KM = 2.0;
    private static final double COST_PER_KG = 5.0;

    /**
     * Calculates delivery cost based on distance and weight.
     * In Phase 1 schema, priority and vehicle details are removed/simplified.
     */
    public double calculateDeliveryCost(int distanceKm, double weightKg, String vehicleType, String priority) {
        double cost = BASE_COST + (distanceKm * COST_PER_KM) + (weightKg * COST_PER_KG);

        // Adjust for vehicle type if provided
        if (vehicleType != null) {
            if (vehicleType.equalsIgnoreCase("Van")) cost *= 1.2;
            else if (vehicleType.equalsIgnoreCase("Truck")) cost *= 1.5;
        }
        
        // Adjust for priority if we have it in description or elsewhere
        if (priority != null) {
            if (priority.equalsIgnoreCase("EXPRESS")) cost *= 1.5;
            else if (priority.equalsIgnoreCase("OVERNIGHT")) cost *= 2.0;
        }

        return Math.round(cost * 100.0) / 100.0;
    }
}
