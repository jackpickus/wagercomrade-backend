package com.jackbets.mybets.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.wager.Wager;

@Repository
@Transactional(readOnly = true)
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>{

    Optional<ApplicationUser> findByUsername(String username);

    @Query(value = "SELECT w from Wager w WHERE w.user = ?1")
    List<Wager> getUsersWagers(ApplicationUser user);

    @Query(value = "SELECT w from Wager w WHERE w.user = ?1 AND w.category = ?2")
    List<Wager> getUsersWagersWithCategory(ApplicationUser user, Category category);
    
}
