package com.jackbets.mybets.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("postgresbets")
public class ApplicationUserDaoService implements ApplicationUserDao {

    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserDaoService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        Optional<ApplicationUser> applicationUser = applicationUserRepository.findByUsername(username);
        return applicationUser;
    }
    
}
