package com.yuriist;

import com.github.javafaker.Faker;
import com.yuriist.customer.Customer;
import com.yuriist.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            for (int i = 0; i < 1; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                Customer customer = new Customer(
                        firstName + " " + lastName,
                        passwordEncoder.encode(UUID.randomUUID().toString()), firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                        random.nextInt(20, 50),
                        random.nextBoolean() ? Customer.Gender.MALE : Customer.Gender.FEMALE
                );
                customerRepository.save(customer);
            }
        };
    }
}
