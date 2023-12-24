package com.jackbets.mybets.registration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.auth.ApplicationUserService;
import com.jackbets.mybets.config.JwtService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final ApplicationUserService applicationUserService;
    private final ApplicationUserRepository applicationUserRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegistrationRequest request) {
        var isValidEmail = emailValidator.test(request.email());
        var isValidPassword = passwordValidator.test(request.password());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        if (!isValidPassword) {
            throw new IllegalStateException("password not valid");
        }

        if (!request.password().equals(request.passwordMatch())) {
            throw new IllegalStateException("passwords do not match");
        }

        var appUser = new ApplicationUser(
                AppUserRole.ROLE_USER,
                request.password(),
                request.username(),
                request.email(), 
                true,
                true,
                true,
                true // TODO make false for email verification
            );

        var jwtToken = jwtService.generateToken(appUser);

        applicationUserService.signUpUser(appUser);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        // if we are here means username and password are correct
        var appUser = applicationUserRepository.findByUsername(request.username());
        var jwtToken = jwtService.generateToken(appUser.get());
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .username(appUser.get().getUsername())
            .build();
    }

}
