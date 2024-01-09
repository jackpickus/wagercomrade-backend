package com.jackbets.mybets.registration;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.auth.ApplicationUserService;
import com.jackbets.mybets.config.JwtService;
import com.jackbets.mybets.mail.MailInfo;
import com.jackbets.mybets.mail.SendEmailConfirmation;
import com.jackbets.mybets.registration.token.ConfirmationToken;
import com.jackbets.mybets.registration.token.ConfirmationTokenService;

import jakarta.transaction.Transactional;
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
    private final ConfirmationTokenService confirmationTokenService;

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
                false
            );

        // var jwtToken = jwtService.generateToken(appUser);

        applicationUserService.signUpUser(appUser);

        return AuthenticationResponse.builder()
            .username(appUser.getUsername())
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

        if (appUser.get().isEnabled()) {
            var jwtToken = jwtService.generateToken(appUser.get());
            return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(appUser.get().getUsername())
                .build();
        } else {
            resendEmail(appUser.get());
            throw new IllegalStateException("User account is not enabled");
            // return AuthenticationResponse.builder()
            //     .token("string")
            //     .username(appUser.get().getUsername())
            //     .build();
        }

    }

    // TODO Enable emails
    // @SendEmailConfirmation
    private MailInfo resendEmail(ApplicationUser appUser) {
        var token = UUID.randomUUID().toString();
        var confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15), // should put num in config file
            appUser 
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return new MailInfo(appUser.getEmail(), token);
    }

    @Transactional
    public String confirmToken(String token) {
        var confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        var expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token has expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        var userIsEnabled = applicationUserService.enableAppUser(confirmationToken.getAppUser().getUsername());
        if (userIsEnabled) {
            return "registration confirmed";
        }
        return "registration failed";
    }

}
