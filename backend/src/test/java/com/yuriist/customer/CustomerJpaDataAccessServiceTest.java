package com.yuriist.customer;

import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        // Given
        Long id = 1L;

        //When
        underTest.getCustomerById(id);

        //Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer("Name", "Email", 10);

        //When
        underTest.insertCustomer(customer);

        //Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = new Faker().internet().safeEmailAddress();

        //When
        underTest.existsPersonWithEmail(email);

        //Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existPersonWithId() {
        // Given
        Long id = 1L;

        //When
        underTest.existPersonWithId(id);

        //Then
        Mockito.verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        Long id = 1L;

        //When
        underTest.deleteCustomerById(id);

        //Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(1L, "Name", "Email", 10);

        //When
        underTest.updateCustomer(customer);

        //Then
        Mockito.verify(customerRepository).save(customer);
    }
}