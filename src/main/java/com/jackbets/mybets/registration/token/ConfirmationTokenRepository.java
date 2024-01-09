package com.jackbets.mybets.registration.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jackbets.mybets.auth.ApplicationUser;


@Repository
public interface ConfirmationTokenRepository
        extends JpaRepository<ConfirmationToken, Long> {

    @Query(value = "SELECT t from ConfirmationToken t WHERE t.token = ?1")
    Optional<ConfirmationToken> findByToken(String token);

    @Query(value = "SELECT u from ConfirmationToken u WHERE u.appUser = ?1")
    Optional<List<ConfirmationToken>> getUsersTokens(ApplicationUser userId);
    
}
