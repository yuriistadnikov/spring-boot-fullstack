package com.yuriist.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        customers.add(new Customer(1L, "Alex", "alex@gmail.com", 26));
        customers.add(new Customer(2L, "Jamila", "jamila@gmail.com", 18));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existPersonWithId(Long id) {
        return customers.stream().anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.removeIf(customer -> customer.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.removeIf(listCustomer -> listCustomer.getId().equals(customer.getId()));
        customers.add(customer);
    }
}
