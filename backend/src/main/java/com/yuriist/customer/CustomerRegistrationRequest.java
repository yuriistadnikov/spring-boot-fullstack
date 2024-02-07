package com.yuriist.customer;

public class CustomerRegistrationRequest {
    private final String name;
    private final String email;
    private final Integer age;
    private final Customer.Gender gender;

    public CustomerRegistrationRequest(String name, String email, Integer age, Customer.Gender gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Customer.Gender getGender() {
        return gender;
    }
}
