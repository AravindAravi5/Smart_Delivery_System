package com.sds.model;

public class Route {
    private int routeId;
    private String sourceCity;
    private String destinationCity;
    private int distance;

    public Route(int routeId, String sourceCity, String destinationCity, int distance) {
        this.routeId = routeId;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.distance = distance;
    }

    public int getRouteId() { return routeId; }
    public String getSourceCity() { return sourceCity; }
    public String getDestinationCity() { return destinationCity; }
    public int getDistance() { return distance; }

    @Override
    public String toString() {
        return sourceCity + " -> " + destinationCity + " (" + distance + " km)";
    }
}
