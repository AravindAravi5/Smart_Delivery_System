package com.sds.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AlgorithmTest {

    private RouteService routeService;

    @BeforeEach
    public void setup() {
        routeService = new RouteService();
    }

    @Test
    public void testGraphAlgorithmsDijkstra() {
        String start = "Hyderabad";
        String end = "Mumbai";

        if (routeService.cityExists(start) && routeService.cityExists(end)) {
            RouteService.RouteResult result = routeService.findShortestPathDijkstra(start, end);
            assertNotNull(result);
            assertTrue(result.totalDistance > 0);

            System.out.println("Shortest path distance: " + result.totalDistance);
        }
    }

    @Test
    public void testBFSPathFinding() {
        String start = "Hyderabad";
        String end = "Mumbai";

        if (routeService.cityExists(start) && routeService.cityExists(end)) {
            List<String> path = routeService.findPathBFS(start, end);
            assertNotNull(path);
            assertFalse(path.isEmpty());
            assertEquals(start, path.get(0));
            assertEquals(end, path.get(path.size() - 1));
        }
    }
}
