package com.jackbets.mybets.wager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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
    private ApplicationUser testUser2;

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
        testUser2 = new ApplicationUser(
            AppUserRole.ROLE_USER,
            "test",
            "test_user2",
            "test2@test.com",
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
    void cannotGetWager() {
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser2));
        when(wagerRepository.findById(anyLong())).thenReturn(Optional.of(wager1));
        testUser2.setId(2L);
        assertThrows(IllegalStateException.class, () ->
            wagerService.getWager(1L, "test_user2"));
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
                new BigDecimal(100),
                120,
                Status.PENDING,
                Instant.now().minus(30, ChronoUnit.MINUTES),
                new BigDecimal(120.0),
                Category.MLB);
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(wagerRepository.save(wager2)).thenReturn(wager2);
        wagerService.addNewWager(wager2, "test_user");
        verify(wagerRepository).save(eq(wager2));
    }

    @Test
    void canDeleteWager() {
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(wagerRepository.existsById(anyLong())).thenReturn(true);
        when(wagerRepository.findById(anyLong())).thenReturn(Optional.of(wager1));
        testUser.setId(1L);
        wagerService.deleteWager(1L, "test_user");
        verify(wagerRepository).deleteById(anyLong());
    }

    @Test
    void cannotDeleteWager() {
        when(appUserRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser2));
        when(wagerRepository.existsById(anyLong())).thenReturn(true);
        when(wagerRepository.findById(anyLong())).thenReturn(Optional.of(wager1));
        testUser2.setId(2L);
        assertThrows(IllegalStateException.class, () ->
            wagerService.deleteWager(1L, "test_user2"));
    }

    @Test
    void canUpdateWagerStatus() {
        when(wagerRepository.findById(anyLong())).thenReturn(Optional.of(wager1));
        HashMap<String, String> updates = new HashMap<>();
        updates.put("status", "WON");
        updates.put("theBet", "49ers -7");
        updates.put("theOdds", "-120");
        wagerService.updateWager(1L, updates);
        assertEquals(Status.WON, wager1.getStatus());
        assertEquals("49ers -7", wager1.getTheBet());
        assertEquals(-120, wager1.getTheOdds());
    }
}