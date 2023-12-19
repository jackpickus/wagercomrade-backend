package com.jackbets.mybets.registration;

import java.util.Objects;

public record AuthenticationRequest(
    String username,
    String password
    ) {

    public AuthenticationRequest {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
    }

}
