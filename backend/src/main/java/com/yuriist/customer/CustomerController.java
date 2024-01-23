package com.yuriist.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomerById(customerId, customerUpdateRequest);
    }

    @PostMapping()
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
    }
}
