package com.sds.service;

import com.sds.dao.CustomerDAO;
import com.sds.model.Customer;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public boolean addCustomer(Customer customer) {
        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            System.out.println("Invalid email format.");
            return false;
        }
        return customerDAO.addCustomer(customer);
    }

    public Optional<Customer> getCustomer(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }
    
    public Optional<Customer> getCustomerByUserId(int userId) {
        return customerDAO.getCustomerByUserId(userId);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int customerId) {
        return customerDAO.deleteCustomer(customerId);
    }
}
