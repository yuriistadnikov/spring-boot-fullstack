package com.yuriist.customer;

import com.yuriist.exceptions.DuplicateResourceException;
import com.yuriist.exceptions.RequestValidationException;
import com.yuriist.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        Mockito.verify(customerDao).getAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 20, Customer.Gender.MALE);
        Mockito.when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer)); //Implement behaviour

        //When
        Customer actual = underTest.getCustomerById(id);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        // Given
        Long id = 10L;
        Mockito.when(customerDao.getCustomerById(id)).thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Couldn't find customer with id %s".formatted(id));
    }

    @Test
    void deleteCustomerById() {
        //Given
        Long id = 10L;
        Mockito.when(customerDao.existPersonWithId(id)).thenReturn(true);

        //When
        underTest.deleteCustomerById(id);

        //Then
        Mockito.verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void exceptionThrownWhenDeleteCustomerByNonExistentId() {
        //Given
        Long id = 10L;
        Mockito.when(customerDao.existPersonWithId(id)).thenReturn(false);

        //Then
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Couldn't find customer with id [%s]".formatted(id));
    }

    @Test
    void updateCustomerNameById() {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerMail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(customerId, customerName, customerMail, customerAge, gender);

        String newCustomerName = "Brand";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(newCustomerName, null, null, null);

        Mockito.when(customerDao.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        //When
        underTest.updateCustomerById(customer.getId(), customerUpdateRequest);

        //Then
        Mockito.verify(customerDao).updateCustomer(
                new Customer(
                        customer.getId(),
                        customerUpdateRequest.getName(),
                        customer.getEmail(),
                        customer.getAge(),
                        customer.getGender()
                )
        );
    }

    @Test
    void updateCustomerEmailById() {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerMail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(customerId, customerName, customerMail, customerAge, gender);

        String newCustomerEmail = "brand@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, newCustomerEmail, null, null);

        Mockito.when(customerDao.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(customerDao.existsPersonWithEmail(customerUpdateRequest.getEmail())).thenReturn(false);

        //When
        underTest.updateCustomerById(customer.getId(), customerUpdateRequest);

        //Then
//        Mockito.verify(customerDao).updateCustomer(
//                new Customer(
//                        customer.getId(),
//                        customer.getName(),
//                        customerUpdateRequest.getEmail(),
//                        customer.getAge()
//                )
//        );

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.getEmail());
        assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerAgeById() {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerMail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(customerId, customerName, customerMail, customerAge, gender);

        Integer newCustomerAge = 25;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, null, newCustomerAge, null);

        Mockito.when(customerDao.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        //When
        underTest.updateCustomerById(customer.getId(), customerUpdateRequest);

        //Then
        Mockito.verify(customerDao).updateCustomer(
                new Customer(
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customerUpdateRequest.getAge(),
                        customer.getGender()
                )
        );
    }

    @Test
    void updateCustomerGenderById() {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerMail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(customerId, customerName, customerMail, customerAge, gender);

        Customer.Gender newGender = Customer.Gender.FEMALE;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, null, null, newGender);

        Mockito.when(customerDao.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        //When
        underTest.updateCustomerById(customer.getId(), customerUpdateRequest);

        //Then
        Mockito.verify(customerDao).updateCustomer(
                new Customer(
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getAge(),
                        customerUpdateRequest.getGender()
                )
        );
    }

    @Test
    void thrownExceptionWheUpdateCustomerEmailById() {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerMail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(customerId, customerName, customerMail, customerAge, gender);

        String newCustomerEmail = "brand@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(customerName, newCustomerEmail, customerAge, gender);

        Mockito.when(customerDao.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(customerDao.existsPersonWithEmail(customerUpdateRequest.getEmail())).thenReturn(true);

        //Then
        assertThatThrownBy(() -> underTest.updateCustomerById(customer.getId(), customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email is already taken");
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void thrownExceptionWheUpdateCustomerByIdWithNoChanges() {
        // Given
        Long customerId = 10L;
        String customerName = "Alex";
        String customerMail = "alex@gmail.com";
        Integer customerAge = 20;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(customerId, customerName, customerMail, customerAge, gender);

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(customerName, customerMail, customerAge, gender);

        Mockito.when(customerDao.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        //Then
        assertThatThrownBy(() -> underTest.updateCustomerById(customer.getId(), customerUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No changes found");
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void addCustomer() {
        // Given
        String email = "alex@gmail.com";
        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest("Alex", email, 20, Customer.Gender.MALE);

        //When
        underTest.addCustomer(customerRegistrationRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customerRegistrationRequest.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistrationRequest.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistrationRequest.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(customerRegistrationRequest.getGender());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "alex@gmail.com";
        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest("Alex", email, 20, Customer.Gender.MALE);

        //Then
        assertThatThrownBy(() -> underTest.addCustomer(customerRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email is already taken");
        Mockito.verify(customerDao, Mockito.never()).insertCustomer(Mockito.any());
    }
}