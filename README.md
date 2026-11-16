# Smart Delivery System

A robust, Java-based enterprise logistics and delivery management system designed to handle the end-to-end lifecycle of package delivery. This system utilizes a structured Model-DAO-Service architecture to seamlessly manage packages, warehouses, delivery routes, and customers.

## 🚀 Features

*   **Package Management & Tracking:** Track packages from origin to destination with real-time status updates and location tracking.
*   **Intelligent Routing:** Calculates the most efficient delivery routes (using Dijkstra's Algorithm/Shortest Path algorithms) to optimize delivery agent travel time and reduce costs.
*   **Dynamic Cost Calculation:** Calculates shipping fees based on dimensions, weight, distance, and priority.
*   **Warehouse & Inventory Management:** Monitor warehouse capacity, incoming shipments, and current stock levels across multiple locations.
*   **Delivery Agent Management:** Assigns packages to couriers based on availability, vehicle type, and current location.
*   **Customer Notifications:** Built-in notification service framework for sending status alerts to customers.
*   **Secure Authentication:** User and employee authentication, password handling, and role-based access.

## 🏗️ Architecture

The project is structured using the standard Java Enterprise **Model-DAO-Service** pattern:

*   `com.sds.model`: Data classes (POJOs) representing the core entities (Package, Customer, Route, Warehouse, DeliveryAgent, etc.).
*   `com.sds.dao`: Data Access Objects responsible for interacting with the MySQL database via JDBC to perform CRUD operations.
*   `com.sds.service`: Core business logic layers that coordinate operations between DAOs, run routing algorithms, calculate costs, and manage workflows.
*   `com.sds.util`: Reusable utilities including database connection pooling, an LRU Cache for performance optimization, multithreaded task execution, and unified logging.

## 🛠️ Technology Stack

*   **Language:** Java (JDK 8+)
*   **Database:** MySQL (JDBC)
*   **Build Tool:** Maven (`pom.xml`)
*   **Architecture:** Core Java, MVC/DAO Pattern

## ⚙️ Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Potluri-Naga-Venkata-Sai/Smart_Delivery_System.git
    cd Smart_Delivery_System
    ```

2.  **Database Configuration:**
    *   Ensure MySQL is installed and running.
    *   Execute the `src/main/resources/schema.sql` script in your MySQL instance to create the necessary tables.
    *   Update the database credentials in `com.sds.util.DatabaseConnection` to match your local setup.

3.  **Build the Project:**
    Since it's a Maven project, you can compile and build it using:
    ```bash
    mvn clean install
    ```

4.  **Run the Application:**
    Execute the main entry point:
    ```bash
    java -cp target/classes com.sds.Main
    ```

## 📝 License

This project is licensed under the MIT License.
