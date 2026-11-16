package com.sds.service;

import com.sds.dao.PackageDAO;
import com.sds.model.Package;

import java.util.ArrayList;
import java.util.List;

public class DashboardService {
    
    private final PackageDAO packageDAO;

    public DashboardService() {
        this.packageDAO = new PackageDAO();
    }

    public void generateRevenueReport() {
        List<Package> packages = packageDAO.getAllPackages();
        double totalRevenue = 0;
        for (Package pkg : packages) {
            if (pkg.getDeliveryCost() != null && "DELIVERED".equalsIgnoreCase(pkg.getStatus())) {
                totalRevenue += pkg.getDeliveryCost();
            }
        }
        System.out.println("====== Dashboard ======");
        System.out.println("Total Packages Processed: " + packages.size());
        System.out.println("Total Revenue (Delivered): ₹" + totalRevenue);
        System.out.println("=======================");
    }

    // DSA: Merge Sort for Packages by Weight
    public List<Package> getPackagesSortedByWeight() {
        List<Package> packages = packageDAO.getAllPackages();
        if (packages == null || packages.isEmpty()) return new ArrayList<>();
        mergeSort(packages, 0, packages.size() - 1);
        return packages;
    }

    private void mergeSort(List<Package> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<Package> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Package> L = new ArrayList<>(n1);
        List<Package> R = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) L.add(list.get(left + i));
        for (int j = 0; j < n2; ++j) R.add(list.get(mid + 1 + j));

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L.get(i).getWeight() <= R.get(j).getWeight()) {
                list.set(k, L.get(i));
                i++;
            } else {
                list.set(k, R.get(j));
                j++;
            }
            k++;
        }
        while (i < n1) { list.set(k, L.get(i)); i++; k++; }
        while (j < n2) { list.set(k, R.get(j)); j++; k++; }
    }

    // DSA: Quick Sort for Packages by Delivery Cost
    public List<Package> getPackagesSortedByCost() {
        List<Package> packages = packageDAO.getAllPackages();
        if (packages == null || packages.isEmpty()) return new ArrayList<>();
        quickSort(packages, 0, packages.size() - 1);
        return packages;
    }

    private void quickSort(List<Package> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private int partition(List<Package> list, int low, int high) {
        double pivot = list.get(high).getDeliveryCost() != null ? list.get(high).getDeliveryCost() : 0.0;
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            double cost = list.get(j).getDeliveryCost() != null ? list.get(j).getDeliveryCost() : 0.0;
            if (cost < pivot) {
                i++;
                Package temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Package temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
}
