package com.jackbets.mybets.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        var confirmationTokenOptional = getToken(token);
        if (confirmationTokenOptional.isPresent()) {
            var confirmationToken = confirmationTokenOptional.get();
            confirmationToken.setConfirmedAt(LocalDateTime.now());
        }
    }
    
}
