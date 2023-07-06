package com.jackbets.mybets.registration;

import java.util.Objects;

public record RegistrationRequest(
    String username,
    String email,
    String password,
    String passwordMatch) {

    public RegistrationRequest {
        Objects.requireNonNull(username);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        Objects.requireNonNull(passwordMatch);
    }

}
