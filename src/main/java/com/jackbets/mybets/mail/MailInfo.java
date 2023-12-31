package com.jackbets.mybets.mail;

import java.util.Objects;

public record MailInfo(String email, String token) {

    public MailInfo {
        Objects.requireNonNull(email);
        Objects.requireNonNull(token);
    }
    
}
