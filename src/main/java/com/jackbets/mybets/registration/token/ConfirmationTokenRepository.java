package com.jackbets.mybets.registration.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfirmationTokenRepository
        extends JpaRepository<ConfirmationToken, Long> {

    @Query(value = "SELECT t from ConfirmationToken t WHERE t.token = ?1")
    Optional<ConfirmationToken> findByToken(String token);
    
}
