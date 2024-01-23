package com.yuriist.customer;

public class CustomerRegistrationRequest {
    private final String name;
    private final String email;
    private final Integer age;

    public CustomerRegistrationRequest(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
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
}
