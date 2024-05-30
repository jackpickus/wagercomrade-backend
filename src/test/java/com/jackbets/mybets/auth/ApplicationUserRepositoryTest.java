package com.jackbets.mybets.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;
import com.jackbets.mybets.wager.Wager;
import com.jackbets.mybets.wager.WagerRepository;

@DataJpaTest
public class ApplicationUserRepositoryTest {

    @Autowired
    private ApplicationUserRepository appUserRepo;

    @Autowired
    private WagerRepository wagerRepository;
    
    private Wager wager1;
    private Wager wager2;
    private ApplicationUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new ApplicationUser(
            AppUserRole.ROLE_USER,
            "test",
            "test_user",
            "test@test.com",
            true,
            true,
            true,
            true);
        wager1 = new Wager("Bears +3.5",
                new BigDecimal(110),
                -110,
                Status.PENDING,
                Instant.now().minus(45, ChronoUnit.MINUTES),
                new BigDecimal(100.0),
                Category.NFL);
        wager2 = new Wager("Cubs ML",
                new BigDecimal(100),
                120,
                Status.PENDING,
                Instant.now().minus(40, ChronoUnit.MINUTES),
                new BigDecimal(120.0),
                Category.MLB);
        wager1.setUser(testUser);
        wager2.setUser(testUser);
    }

    @AfterEach
    void tearDown() {
        // the order matters!
        wagerRepository.deleteAll();
        appUserRepo.deleteAll();
    }
   
    @Test
    void itShouldGetUsersWagers() {
        var actual = List.of(wager1, wager2);
        appUserRepo.save(testUser);
        wagerRepository.save(wager1);
        wagerRepository.save(wager2);
        var expected = appUserRepo.getUsersWagers(testUser);
        assertEquals(actual, expected);
    }

    @Test
    void itShouldGetUsersWagersByCategory() {
        var actual = List.of(wager2);
        appUserRepo.save(testUser);
        wagerRepository.save(wager1);
        wagerRepository.save(wager2);
        var expected = appUserRepo.getUsersWagersWithCategory(testUser, Category.MLB);
        assertEquals(actual, expected);
    }

}
