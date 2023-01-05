package com.jackbets.mybets.auth;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository("postgresbets")
public class ApplicationUserDaoService implements ApplicationUserDao{

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return Optional.empty();
    }
    
}
