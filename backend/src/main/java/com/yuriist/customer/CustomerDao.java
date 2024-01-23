package com.yuriist.customer;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public interface CustomerDao {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Long customerId);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);

    boolean existPersonWithId(Long id);
    void deleteCustomerById(Long id);
    void updateCustomer(Customer customer);
}
