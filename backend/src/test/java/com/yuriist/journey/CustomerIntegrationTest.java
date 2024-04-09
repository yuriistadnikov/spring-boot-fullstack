package com.yuriist.journey;

import com.github.javafaker.Faker;
import com.yuriist.customer.Customer;
import com.yuriist.customer.CustomerDTO;
import com.yuriist.customer.CustomerRegistrationRequest;
import com.yuriist.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.swing.text.html.HTML;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    private static final Random RANDOM = new Random();
    public static final String CUSTOMER_PATH = "/api/v1/customers";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterACustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        Integer age = RANDOM.nextInt(18, 48);
        Customer.Gender gender = RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, "Pass123456", email, age, gender);

        // Send a post request
        String jwtToken = webTestClient.post().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // Get all customers
        List<CustomerDTO> allCustomers = webTestClient.get().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // Make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email
        );
        assertThat(allCustomers)
                .contains(expectedCustomer);

        // Get customer by ID
        webTestClient.get().uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
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
        Customer.Gender gender = RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, "Pass123456", email, age, gender);

        // Send a post request
        webTestClient.post().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Create customer #2
        String secondName = faker.name().fullName();
        String secondEmail = faker.internet().safeEmailAddress();
        Integer secondAge = RANDOM.nextInt(18, 48);
        Customer.Gender secondGender = RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        CustomerRegistrationRequest secondRequest = new CustomerRegistrationRequest(secondName, "Pass123456", secondEmail, secondAge, secondGender);

        // Send a post request
        String jwtToken = webTestClient.post().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(secondRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // Get all customers
        List<CustomerDTO> allCustomers = webTestClient.get().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // Delete customer by ID
        webTestClient.delete().uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        // Get customer by ID
        webTestClient.get().uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
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
        Customer.Gender gender = RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, "Pass123456", email, age, gender);

        // Send a post request
        webTestClient.post().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Create customer #2
        String secondName = faker.name().fullName();
        String secondEmail = faker.internet().safeEmailAddress();
        Integer secondAge = RANDOM.nextInt(18, 48);
        Customer.Gender secondGender = RANDOM.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        CustomerRegistrationRequest secondRequest = new CustomerRegistrationRequest(secondName, "Pass123456", secondEmail, secondAge, secondGender);

        // Send a post request
        String jwtToken = webTestClient.post().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(secondRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // Get all customers
        List<CustomerDTO> allCustomers = webTestClient.get().uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // Update customer
        String newName = faker.name().fullName();
        String newEmail = faker.internet().safeEmailAddress();
        Integer newAge = RANDOM.nextInt(18, 48);
        Customer.Gender newGender = gender.equals(Customer.Gender.FEMALE) ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName, newEmail, newAge, newGender);

        webTestClient.put().uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        // Verify customer changes
        CustomerDTO expectedCustomer = new CustomerDTO(id, newName, newEmail, newGender, newAge, List.of("ROLE_USER"), newEmail);

        webTestClient.get().uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(expectedCustomer);
    }
}
