package com.yuriist.customer;

public class CustomerRegistrationRequest {
    private final String name;
    private final String password;
    private final String email;
    private final Integer age;
    private final Customer.Gender gender;

    public CustomerRegistrationRequest(String name, String password, String email, Integer age, Customer.Gender gender) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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
