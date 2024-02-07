package com.yuriist.customer;

import com.yuriist.AbstractTestContainer;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                RANDOM.nextInt(18, 40),
                RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE
        );
        underTest.save(customer);

        //When
        boolean isCustomerExists = underTest.existsCustomerByEmail(customer.getEmail());

        //Then
        assertThat(isCustomerExists).isTrue();
    }

    @Test
    void existsCustomerById() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                RANDOM.nextInt(18, 40),
                RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE
        );
        underTest.save(customer);
        long customerId = underTest.findAll()
                .stream()
                .filter(dbCustomer -> dbCustomer.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        boolean isCustomerExists = underTest.existsCustomerById(customerId);

        //Then
        assertThat(isCustomerExists).isTrue();
    }
}