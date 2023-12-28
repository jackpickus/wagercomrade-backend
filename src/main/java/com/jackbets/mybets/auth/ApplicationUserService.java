package com.jackbets.mybets.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jackbets.mybets.mail.ConfirmRegister;

@Service
public class ApplicationUserService implements UserDetailsService{

    private final ApplicationUserDao applicationUserDao;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUserService(@Qualifier("postgresbets") ApplicationUserDao applicationUserDao,
        PasswordEncoder passwordEncoder) {

        this.applicationUserDao = applicationUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDao
            .selectApplicationUserByUsername(username)
            .orElseThrow(() -> 
                new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    @ConfirmRegister
    public String signUpUser(ApplicationUser applicationUser) {

        var userExists = applicationUserDao.selectApplicationUserByUsername(applicationUser.getUsername()).isPresent();

        if (userExists) {
            throw new IllegalStateException("username already taken");
        }

        var encodedPassword = passwordEncoder.encode(applicationUser.getPassword());

        applicationUser.setPassword(encodedPassword);

        applicationUserDao.save(applicationUser);

        // TODO send confirmation token

        return "signUpUser() works!";
    }
    
}
