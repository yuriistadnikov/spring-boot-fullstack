package com.yuriist.journey;

import com.github.javafaker.Faker;
import com.yuriist.customer.Customer;
import com.yuriist.customer.CustomerRegistrationRequest;
import com.yuriist.customer.CustomerUpdateRequest;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    private static final Random RANDOM = new Random();

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterACustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        Integer age = RANDOM.nextInt(18, 48);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        // Send a post request
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customers
        List<Customer> allCustomers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // Make sure that customer is present
        Customer expectedCustomer = new Customer(name, email, age);
        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        expectedCustomer.setId(id);

        // Get customer by ID
        webTestClient.get().uri("/api/v1/customers" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }


    @Test
    void canDeleteCustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        Integer age = RANDOM.nextInt(18, 48);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        // Send a post request
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customers
        List<Customer> allCustomers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // Delete customer by ID
        webTestClient.delete().uri("/api/v1/customers" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // Get customer by ID
        webTestClient.get().uri("/api/v1/customers" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        Integer age = RANDOM.nextInt(18, 48);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        // Send a post request
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customers
        List<Customer> allCustomers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // Update customer
        String newName = faker.name().fullName();
        String newEmail = faker.internet().safeEmailAddress();
        Integer newAge = RANDOM.nextInt(18, 48);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName, newEmail, newAge);

        webTestClient.put().uri("/api/v1/customers" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Verify customer changes
        Customer expectedCustomer = new Customer(id, newName, newEmail, newAge);
        webTestClient.get().uri("/api/v1/customers" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .isEqualTo(expectedCustomer);
    }
}
