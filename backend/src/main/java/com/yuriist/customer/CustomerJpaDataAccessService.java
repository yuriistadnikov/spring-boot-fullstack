package com.yuriist.customer;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existPersonWithId(Long id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
