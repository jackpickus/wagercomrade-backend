package com.jackbets.mybets.registration.token;

public class ConfirmationTokenNotFoundException extends RuntimeException {

    ConfirmationTokenNotFoundException(String confirmationToken) {
        super("Could not find confirmation token: " + confirmationToken);
    }

    
}
