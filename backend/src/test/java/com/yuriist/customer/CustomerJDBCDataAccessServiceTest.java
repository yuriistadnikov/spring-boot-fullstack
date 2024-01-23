package com.yuriist.customer;

import com.yuriist.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void getAllCustomers() {
        // Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);

        //When
        List<Customer> customers = underTest.getAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        // Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);
        Long customerId = getCustomerIdByEmail(customer.getEmail());

        //When
        Optional<Customer> customerOptional = underTest.getCustomerById(customerId);

        //Then
        assertThat(customerOptional).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customerId);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        Long id = -1L;

        //When
        Optional<Customer> customerById = underTest.getCustomerById(id);

        //Then
        assertThat(customerById).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = getRandomCustomer();

        //When
        underTest.insertCustomer(customer);

        Optional<Customer> addedCustomer = underTest.getAllCustomers()
                .stream()
                .filter(newCustomer -> newCustomer.getEmail().equals(customer.getEmail()))
                .findFirst();

        //Then
        assertThat(addedCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);

        //When
       boolean isCustomerPresent = underTest.existsPersonWithEmail(customer.getEmail());

        //Then
        assertThat(isCustomerPresent).isTrue();
    }

    @Test
    void notExistsPersonWithEmail() {
        assertThat(underTest.existsPersonWithEmail(FAKER.internet().safeEmailAddress())).isFalse();
    }

    @Test
    void existPersonWithId() {
        // Given
        Customer customer = getRandomCustomer();

        //When
        underTest.insertCustomer(customer);
        Long customerId = getCustomerIdByEmail(customer.getEmail());
        boolean isCustomerPresent = underTest.existPersonWithId(customerId);

        //Then
        assertThat(isCustomerPresent).isTrue();
    }

    @Test
    void notExistPersonWithId() {
        assertThat(underTest.existPersonWithId(0L)).isFalse();
    }


    @Test
    void deleteCustomerById() {
        // Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);
        Long customerId = getCustomerIdByEmail(customer.getEmail());

        //When
        underTest.deleteCustomerById(customerId);
        boolean isCustomerExist = underTest.existPersonWithId(customerId);

        //Then
        assertThat(isCustomerExist).isFalse();
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);

        Customer addedCustomer = underTest.getCustomerById(getCustomerIdByEmail(customer.getEmail()))
                .orElseThrow();

        addedCustomer.setName(FAKER.name().fullName());
        addedCustomer.setEmail(FAKER.internet().safeEmailAddress());
        addedCustomer.setAge(customer.getAge() + RANDOM.nextInt(2, 10));

        //When
        underTest.updateCustomer(addedCustomer);
        Customer updatedCustomer = underTest.getCustomerById(addedCustomer.getId())
                .orElseThrow();

        //Then
        assertThat(updatedCustomer.getName().equals(addedCustomer.getName())).isTrue();
        assertThat(updatedCustomer.getEmail().equals(addedCustomer.getEmail())).isTrue();
        assertThat(updatedCustomer.getAge().equals(addedCustomer.getAge())).isTrue();
    }

    @Test
    void updateCustomerName() {
        //Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);

        Customer addedCustomer = underTest.getCustomerById(getCustomerIdByEmail(customer.getEmail()))
                .orElseThrow();

        addedCustomer.setName(FAKER.name().fullName());

        //Then
        underTest.updateCustomer(addedCustomer);

        Customer updatedCustomer = underTest.getCustomerById(addedCustomer.getId())
                .orElseThrow();

        //When
        assertThat(updatedCustomer.getName().equals(addedCustomer.getName())).isTrue();
        assertThat(updatedCustomer.getEmail().equals(addedCustomer.getEmail())).isTrue();
        assertThat(updatedCustomer.getAge().equals(addedCustomer.getAge())).isTrue();
    }

    @Test
    void updateCustomerAge() {
        //Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);
        Customer addedCustomer = underTest.getCustomerById(getCustomerIdByEmail(customer.getEmail()))
                .orElseThrow();
        addedCustomer.setAge(customer.getAge() + RANDOM.nextInt(2, 10));

        //When
        underTest.updateCustomer(addedCustomer);
        Customer updatedCustomer = underTest.getCustomerById(addedCustomer.getId())
                .orElseThrow();

        //Then
        assertThat(updatedCustomer.getName().equals(addedCustomer.getName())).isTrue();
        assertThat(updatedCustomer.getEmail().equals(addedCustomer.getEmail())).isTrue();
        assertThat(updatedCustomer.getAge().equals(addedCustomer.getAge())).isTrue();
    }

    @Test
    void updateCustomerEmail() {
        //Given
        Customer customer = getRandomCustomer();
        underTest.insertCustomer(customer);
        Customer addedCustomer = underTest.getCustomerById(getCustomerIdByEmail(customer.getEmail()))
                .orElseThrow();
        addedCustomer.setEmail(FAKER.internet().safeEmailAddress());

        //When
        underTest.updateCustomer(addedCustomer);
        Customer updatedCustomer = underTest.getCustomerById(addedCustomer.getId())
                .orElseThrow();

        //Then
        assertThat(updatedCustomer.getName().equals(addedCustomer.getName())).isTrue();
        assertThat(updatedCustomer.getEmail().equals(addedCustomer.getEmail())).isTrue();
        assertThat(updatedCustomer.getAge().equals(addedCustomer.getAge())).isTrue();
    }

    private Customer getRandomCustomer() {
        return new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                RANDOM.nextInt(18, 40)
        );
    }

    private Long getCustomerIdByEmail(String email) {
        return underTest.getAllCustomers().stream()
                .filter(dbCustomer -> dbCustomer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
    }
}