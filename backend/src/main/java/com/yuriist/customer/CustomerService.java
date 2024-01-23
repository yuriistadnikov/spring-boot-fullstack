package com.yuriist.customer;

import com.yuriist.exceptions.DuplicateResourceException;
import com.yuriist.exceptions.RequestValidationException;
import com.yuriist.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public Customer getCustomerById(Long customerId) {
        return customerDao.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find customer with id %s".formatted(customerId)));

    }

    public void deleteCustomerById(Long customerId) {
        if (!customerDao.existPersonWithId(customerId)) {
            throw new ResourceNotFoundException("Couldn't find customer with id [%s]".formatted(customerId));
        }
        customerDao.deleteCustomerById(customerId);
    }

    public void updateCustomerById(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomerById(customerId);
        boolean hasChanges = false;
        if (StringUtils.hasText(customerUpdateRequest.getName()) && !customerUpdateRequest.getName().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.getName());
            hasChanges = true;
        }
        if (StringUtils.hasText(customerUpdateRequest.getEmail()) && !customerUpdateRequest.getEmail().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(customerUpdateRequest.getEmail())) {
                throw new DuplicateResourceException("Email is already taken");
            }
            customer.setEmail(customerUpdateRequest.getEmail());
            hasChanges = true;
        }
        if (Objects.nonNull(customerUpdateRequest.getAge()) && !customerUpdateRequest.getAge().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.getAge());
            hasChanges = true;
        }
        if (hasChanges) {
            customerDao.updateCustomer(customer);
        } else {
            throw new RequestValidationException("No changes found");
        }
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.getEmail())) {
            throw new DuplicateResourceException("Email is already taken");
        }
        customerDao.insertCustomer(
                new Customer(
                        customerRegistrationRequest.getName(),
                        customerRegistrationRequest.getEmail(),
                        customerRegistrationRequest.getAge()
                )
        );
    }
}
