package com.yuriist.customer;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerRowMapperTest {

    private CustomerRowMapper underTest;

    @Mock
    private ResultSet resultSet;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    public CustomerRowMapperTest() {
        this.underTest = new CustomerRowMapper();
    }

    @Test
    void mapRow() throws SQLException {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerEmail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;

        Mockito.when(resultSet.getLong("id")).thenReturn(customerId);
        Mockito.when(resultSet.getString("name")).thenReturn(customerName);
        Mockito.when(resultSet.getString("email")).thenReturn(customerEmail);
        Mockito.when(resultSet.getInt("age")).thenReturn(customerAge);
        Mockito.when(resultSet.getString("gender")).thenReturn(gender.toString());


        //When
        Customer customer = underTest.mapRow(resultSet, 0);

        //Then
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(customerId);
        assertThat(customer.getName()).isEqualTo(customerName);
        assertThat(customer.getEmail()).isEqualTo(customerEmail);
        assertThat(customer.getAge()).isEqualTo(customerAge);
        assertThat(customer.getGender()).isEqualTo(gender);
    }
}