package com.jackbets.mybets.auth;

public class UsernameExistsException extends RuntimeException {

    UsernameExistsException(String username) {
        super("Username already exists: " + username);
    }
    
}
