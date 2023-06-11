package com.jackbets.mybets.registration;

import org.springframework.stereotype.Service;

import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final ApplicationUserService applicationUserService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        var isValidEmail = emailValidator.test(request.email());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        return applicationUserService.signUpUser(
            new ApplicationUser(
                AppUserRole.ROLE_USER,
                request.password(),
                request.username(),
                request.email(), 
                true,
                true,
                true,
                true // TODO make false for email verification
            )
        );

    }

}
