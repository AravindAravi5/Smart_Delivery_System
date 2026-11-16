package com.sds.service;

import com.sds.model.Route;
import com.sds.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RouteService {
    
    private Set<String> cities = new HashSet<>();
    private Map<String, List<Route>> adjacencyList = new HashMap<>();

    public RouteService() {
        loadGraphFromDatabase();
    }

    private void loadGraphFromDatabase() {
        String routeSql = "SELECT * FROM Routes";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(routeSql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String source = rs.getString("source_city");
                String dest = rs.getString("destination_city");
                int distance = rs.getInt("distance");

                cities.add(source);
                cities.add(dest);

                adjacencyList.putIfAbsent(source, new ArrayList<>());
                adjacencyList.putIfAbsent(dest, new ArrayList<>());

                Route route = new Route(rs.getInt("route_id"), source, dest, distance);
                adjacencyList.get(source).add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean cityExists(String name) {
        return cities.contains(name);
    }

    public List<String> findPathBFS(String start, String target) {
        if (!cities.contains(start) || !cities.contains(target)) return null;

        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                return constructPath(parentMap, start, target);
            }

            for (Route edge : adjacencyList.get(current)) {
                String neighbor = edge.getDestinationCity();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return null; // No path found
    }

    public RouteResult findShortestPathDijkstra(String start, String target) {
        if (!cities.contains(start) || !cities.contains(target)) return null;

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();
        PriorityQueue<CityDistanceNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));

        for (String city : cities) {
            distances.put(city, Integer.MAX_VALUE);
        }
        
        distances.put(start, 0);
        pq.add(new CityDistanceNode(start, 0));

        while (!pq.isEmpty()) {
            CityDistanceNode current = pq.poll();
            String u = current.city;

            if (u.equals(target)) break; 

            if (current.distance > distances.get(u)) continue;

            for (Route edge : adjacencyList.getOrDefault(u, new ArrayList<>())) {
                String v = edge.getDestinationCity();
                int weight = edge.getDistance();
                
                if (distances.get(u) + weight < distances.get(v)) {
                    distances.put(v, distances.get(u) + weight);
                    parentMap.put(v, u);
                    pq.add(new CityDistanceNode(v, distances.get(v)));
                }
            }
        }

        if (distances.get(target) == Integer.MAX_VALUE) return null;

        List<String> path = constructPath(parentMap, start, target);
        return new RouteResult(path, distances.get(target));
    }

    private List<String> constructPath(Map<String, String> parentMap, String start, String target) {
        LinkedList<String> path = new LinkedList<>();
        String curr = target;
        while (curr != null) {
            path.addFirst(curr);
            curr = parentMap.get(curr);
        }
        return path;
    }

    private static class CityDistanceNode {
        String city;
        int distance;
        CityDistanceNode(String city, int distance) {
            this.city = city;
            this.distance = distance;
        }
    }

    public static class RouteResult {
        public List<String> path;
        public int totalDistance;
        public RouteResult(List<String> path, int totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }
}
