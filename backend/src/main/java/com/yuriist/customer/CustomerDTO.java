package com.yuriist.customer;

import java.util.List;

public record CustomerDTO (
    Long id,
    String name,
    String email,
    Customer.Gender gender,
    Integer age,
    List<String> roles,
    String username
){}
