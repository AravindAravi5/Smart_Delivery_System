package com.sds;

import com.sds.model.*;
import com.sds.model.Package;
import com.sds.service.*;
import com.sds.util.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final CustomerService customerService = new CustomerService();
    private static final PackageService packageService = new PackageService();
    private static final RouteService routeService = new RouteService();
    private static final InventoryService inventoryService = new InventoryService(routeService);
    private static final DeliveryAgentService agentService = new DeliveryAgentService();
    private static final NotificationService notificationService = new NotificationService();
    private static final DispatchService dispatchService = new DispatchService(packageService, agentService,
            notificationService);
    private static final DashboardService dashboardService = new DashboardService();

    private static final LRUCache<Integer, Customer> customerCache = new LRUCache<>(100);

    public static void main(String[] args) {
        LoggerUtil.info("Application Started");
        System.out.println("===========================================");
        System.out.println("  Smart Route & Delivery Management System ");
        System.out.println("===========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    running = false;
                    TaskExecutor.shutdown();
                    System.out.println("Exiting System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Role (ADMIN, CUSTOMER, AGENT, EMPLOYEE): ");
        User.Role role = User.Role.valueOf(scanner.nextLine().toUpperCase());

        if (authService.register(username, password, email, role)) {
            System.out.println("Registration successful!");
            LoggerUtil.info("New user registered: " + username);
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Login successful. Welcome " + user.getUsername() + "!");
            LoggerUtil.info("User logged in: " + username);

            if (user.getRole() == User.Role.ADMIN) {
                adminMenu();
            } else if (user.getRole() == User.Role.CUSTOMER) {
                customerMenu(user);
            } else if (user.getRole() == User.Role.AGENT) {
                agentMenu(user);
            } else {
                System.out.println("Menu for this role is under construction.");
            }
        }
    }

    private static void adminMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. View Revenue Report");
            System.out.println("2. Process Dispatch Queue");
            System.out.println("3. Logout");
            System.out.print("Select: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    dashboardService.generateRevenueReport();
                    break;
                case 2:
                    dispatchService.processDispatch();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void agentMenu(User user) {
        Optional<DeliveryAgent> optAgent = agentService.getAgentByUserId(user.getUserId());
        DeliveryAgent agent;
        if (optAgent.isEmpty()) {
            System.out.println("Please complete your agent profile.");
            System.out.print("Full Name: ");
            String name = scanner.nextLine();
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Vehicle Type (e.g. Van, Bike): ");
            String vehicleType = scanner.nextLine();
            System.out.print("License Number: ");
            String licenseNumber = scanner.nextLine();
            System.out.print("City: ");
            String city = scanner.nextLine();

            agent = new DeliveryAgent(0, user.getUserId(), name, phone, user.getEmail(), vehicleType, licenseNumber,
                    city, "AVAILABLE", null);
            agentService.addAgent(agent);

            // Re-fetch to get the generated agent ID
            optAgent = agentService.getAgentByUserId(user.getUserId());
            agent = optAgent.orElse(agent);
            System.out.println("Agent Profile created.");
        } else {
            agent = optAgent.get();
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Agent Menu ---");
            System.out.println("1. View My Assigned Packages");
            System.out.println("2. Mark Package as DELIVERED");
            System.out.println("3. Logout");
            System.out.print("Select: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    List<Package> myPackages = packageService.getPackagesByAgentId(agent.getAgentId());
                    if (myPackages.isEmpty()) {
                        System.out.println("You have no packages assigned.");
                    } else {
                        System.out.println("\n--- Assigned Packages ---");
                        for (Package p : myPackages) {
                            System.out.println("ID: " + p.getPackageId() + " | Tracking: " + p.getTrackingId()
                                    + " | Destination: " + p.getDestinationCity() + " | Status: " + p.getStatus());
                        }
                    }
                    break;
                case 2:
                    System.out.print("Enter Package ID (the number, e.g. 1) to mark as DELIVERED: ");
                    try {
                        int pkgId = Integer.parseInt(scanner.nextLine());
                        Optional<Package> optPkg = packageService.getPackage(pkgId);

                        if (optPkg.isPresent()) {
                            Package pkg = optPkg.get();
                            if (pkg.getAgentId() != null && pkg.getAgentId() == agent.getAgentId()) {
                                if (!"DELIVERED".equals(pkg.getStatus())) {
                                    pkg.setStatus("DELIVERED");
                                    if (packageService.updatePackage(pkg)) {
                                        System.out.println("Package " + pkg.getTrackingId()
                                                + " successfully marked as DELIVERED.");
                                        agent.setStatus("AVAILABLE");
                                        agentService.updateAgent(agent);
                                    } else {
                                        System.out.println("Failed to update package status.");
                                    }
                                } else {
                                    System.out.println("Package is already DELIVERED.");
                                }
                            } else {
                                System.out.println("This package is not assigned to you.");
                            }
                        } else {
                            System.out.println("Package not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter the numeric Package ID.");
                    }
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void customerMenu(User user) {
        Optional<Customer> optCustomer = customerService.getCustomerByUserId(user.getUserId());
        Customer customer;
        if (optCustomer.isEmpty()) {
            System.out.println("Please complete your profile.");
            System.out.print("Full Name: ");
            String name = scanner.nextLine();
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Address: ");
            String address = scanner.nextLine();
            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("Pincode: ");
            String pincode = scanner.nextLine();

            customer = new Customer(0, user.getUserId(), name, phone, user.getEmail(), address, city, pincode, null);
            customerService.addCustomer(customer);
            System.out.println("Profile created.");
        } else {
            customer = optCustomer.get();
        }

        customerCache.put(customer.getCustomerId(), customer);

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Send a Package");
            System.out.println("2. View My Profile");
            System.out.println("3. Logout");
            System.out.print("Select: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    sendPackage(customer);
                    break;
                case 2:
                    System.out.println(customerCache.get(customer.getCustomerId()));
                    break;
                case 3:
                    back = true;
                    break;
            }
        }
    }

    private static void sendPackage(Customer customer) {
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Receiver City: ");
        String toCity = scanner.nextLine();
        System.out.print("Weight (kg): ");
        double weight = Double.parseDouble(scanner.nextLine());

        Package pkg = new Package();
        pkg.setTrackingId("TRK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pkg.setCustomerId(customer.getCustomerId());
        pkg.setDescription(description);
        pkg.setSourceCity(customer.getCity());
        pkg.setDestinationCity(toCity);
        pkg.setWeight(weight);
        pkg.setStatus("PENDING");

        if (routeService.cityExists(customer.getCity()) && routeService.cityExists(toCity)) {
            RouteService.RouteResult route = routeService.findShortestPathDijkstra(customer.getCity(), toCity);
            if (route != null) {
                CostCalculatorService costService = new CostCalculatorService();
                // Assumed standard priority and Van
                double cost = costService.calculateDeliveryCost(route.totalDistance, weight, null, null);
                pkg.setDeliveryCost(cost);
                System.out.println("Calculated Cost: ₹" + cost + " (Distance: " + route.totalDistance + " km)");
            } else {
                System.out.println("No route found between cities. Defaulting cost.");
                pkg.setDeliveryCost(500.0);
            }
        } else {
            pkg.setDeliveryCost(500.0);
        }

        if (packageService.addPackage(pkg)) {
            System.out.println("Package added successfully! Tracking ID: " + pkg.getTrackingId());

            TaskExecutor.submitTask(() -> {
                FileHandler.generateInvoice(pkg);
            });

            Warehouse bestWarehouse = inventoryService.allocateBestWarehouse(pkg);
            if (bestWarehouse != null) {
                inventoryService.storePackageInWarehouse(bestWarehouse, pkg, "A1", "Outbound");
                System.out.println("Allocated to Warehouse: " + bestWarehouse.getWarehouseName());
            }

            dispatchService.addToDispatchQueue(pkg);
        } else {
            System.out.println("Failed to add package.");
        }
    }
}
