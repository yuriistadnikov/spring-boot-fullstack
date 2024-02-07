package com.yuriist.customer;

public class CustomerUpdateRequest {
    private String name;
    private String email;
    private Integer age;
    private Customer.Gender gender;

    public CustomerUpdateRequest() {
    }

    public CustomerUpdateRequest(String name, String email, Integer age, Customer.Gender gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Customer.Gender getGender() {
        return gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(Customer.Gender gender) {
        this.gender = gender;
    }
}
