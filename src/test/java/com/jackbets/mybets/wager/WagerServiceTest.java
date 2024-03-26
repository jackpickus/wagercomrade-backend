package com.jackbets.mybets.wager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;

@ExtendWith(MockitoExtension.class)
class WagerServiceTest {

    @Mock
    private WagerRepository wagerRepository;

    @Mock
    private ApplicationUserRepository appUserRepo;

    private WagerService wagerService;

    private Wager wager1;

    private ApplicationUser testUser;

    @BeforeEach
    void setUp() {
        wagerService = new WagerService(wagerRepository, appUserRepo);

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
                110,
                -110,
                Status.PENDING,
                Instant.now().minus(45, ChronoUnit.MINUTES),
                // LocalDateTime.of(2022, Month.JULY, 2, 8, 15, 23),
                100.0,
                Category.NFL);
        wager1.setUser(testUser);
    }

    @Test
    void canGetWager() {
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(wagerRepository.findById(anyLong())).thenReturn(Optional.of(wager1));
        wagerService.getWager(1L, anyString());
        verify(wagerRepository).findById(1L);
    }

    @Test
    void canGetAllOfUserWagers() {
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        wagerService.getUsersWagers("test_user");
        verify(appUserRepo).getUsersWagers(testUser);
    }

    @Test
    void getWagersWithCategory() {
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        wagerService.getWagersWithCategory("test_user", Category.NFL);
        verify(appUserRepo).getUsersWagersWithCategory(eq(testUser), eq(Category.NFL));
    }

    @Test
    void canAddNewWager() {
        var wager2 = new Wager("Cubs ML",
                100,
                120,
                Status.PENDING,
                Instant.now().minus(30, ChronoUnit.MINUTES),
                120.0,
                Category.MLB);
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(wagerRepository.save(wager2)).thenReturn(wager2);
        wagerService.addNewWager(wager2, "test_user");
        verify(wagerRepository).save(eq(wager2));
    }

    @Test
    void deleteWager() {

    }

    @Test
    void updateWager() {

    }
}