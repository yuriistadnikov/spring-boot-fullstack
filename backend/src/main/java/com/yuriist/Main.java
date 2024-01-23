package com.yuriist;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.yuriist.customer.Customer;
import com.yuriist.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            for (int i = 0; i < 1; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                Customer customer = new Customer(
                        firstName + " " + lastName,
                        firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                        random.nextInt(20, 50)
                );
                customerRepository.save(customer);
            }
        };
    }
}
