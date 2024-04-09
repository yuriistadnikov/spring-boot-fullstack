package com.yuriist.auth;

import com.yuriist.customer.CustomerDTO;

public record AuthenticationResponse(CustomerDTO customerDTO, String token) {
}
