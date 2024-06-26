package com.jackbets.mybets.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jackbets.mybets.mail.MailInfo;
import com.jackbets.mybets.mail.SendEmailConfirmation;
import com.jackbets.mybets.registration.token.ConfirmationToken;
import com.jackbets.mybets.registration.token.ConfirmationTokenService;

@Service
public class ApplicationUserService implements UserDetailsService{

    private final ApplicationUserDao applicationUserDao;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public ApplicationUserService(@Qualifier("postgresbets") ApplicationUserDao applicationUserDao,
        PasswordEncoder passwordEncoder,
        ConfirmationTokenService confirmationTokenService) {

        this.applicationUserDao = applicationUserDao;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDao
            .selectApplicationUserByUsername(username)
            .orElseThrow(() -> 
                new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    @SendEmailConfirmation
    public MailInfo signUpUser(ApplicationUser applicationUser) {

        var userExists = applicationUserDao.selectApplicationUserByUsername(applicationUser.getUsername()).isPresent();

        if (userExists) {
            throw new UsernameExistsException(applicationUser.getUsername());
        }

        var encodedPassword = passwordEncoder.encode(applicationUser.getPassword());

        applicationUser.setPassword(encodedPassword);

        applicationUserDao.save(applicationUser);

        var token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            Instant.now(),
            Instant.now().plus(15, ChronoUnit.MINUTES), // should put num in config file
            applicationUser 
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return new MailInfo(applicationUser.getEmail(), token);
    }

    public boolean enableAppUser(String username) {
        var appUserOptional = applicationUserDao.selectApplicationUserByUsername(username);
        if (appUserOptional.isPresent()) {
            var appUser = appUserOptional.get();
            appUser.setEnabled(true);
            return true;
        }
        return false;
    }
    
}
