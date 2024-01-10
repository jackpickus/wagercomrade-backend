package com.jackbets.mybets.registration.token;

public class ConfirmationTokenException extends RuntimeException {

    public ConfirmationTokenException() {
        super("The confirmation token is not valid");
    }
    
}
