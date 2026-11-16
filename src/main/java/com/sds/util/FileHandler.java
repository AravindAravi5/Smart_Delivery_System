package com.sds.util;

import com.sds.model.Package;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileHandler {

    public static void generateInvoice(Package pkg) {
        String filename = "Invoice_" + pkg.getTrackingId() + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=========================================");
            writer.println("              TAX INVOICE                ");
            writer.println("=========================================");
            writer.println("Tracking ID: " + pkg.getTrackingId());
            writer.println("Description: " + pkg.getDescription());
            writer.println("Weight: " + pkg.getWeight() + " kg");
            writer.println("From: " + pkg.getSourceCity());
            writer.println("To: " + pkg.getDestinationCity());
            writer.println("-----------------------------------------");
            writer.println("Total Cost: Rs." + (pkg.getDeliveryCost() != null ? pkg.getDeliveryCost() : "N/A"));
            writer.println("=========================================");
            LoggerUtil.info("Generated invoice: " + filename);
        } catch (IOException e) {
            LoggerUtil.error("Failed to generate invoice for " + pkg.getTrackingId(), e);
        }
    }

    public static void exportPackagesToCSV(List<Package> packages) {
        String filename = "Packages_Report.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Tracking ID,From,To,Weight,Cost,Status");
            for (Package p : packages) {
                writer.printf("%s,%s,%s,%.2f,%.2f,%s\n",
                    p.getTrackingId(),
                    p.getSourceCity(), p.getDestinationCity(),
                    p.getWeight(), 
                    (p.getDeliveryCost() != null ? p.getDeliveryCost() : 0.0),
                    p.getStatus());
            }
            LoggerUtil.info("Exported " + packages.size() + " packages to CSV.");
        } catch (IOException e) {
            LoggerUtil.error("Failed to export packages.", e);
        }
    }
}
