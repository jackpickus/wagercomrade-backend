package com.jackbets.mybets.wager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;

class WagerServiceTest {

    @Mock
    private WagerRepository wagerRepository;

    @Mock
    private ApplicationUserRepository appUserRepo;

    private WagerService wagerService;

    private Wager wager1;
    private Wager wager2;
    List<Wager> wagerList;
    private ApplicationUser testUser;

    @BeforeEach
    public void setUp() {
        wagerService = new WagerService(wagerRepository, appUserRepo);

        wagerList = new ArrayList<>();
        wager1 = new Wager("Bears +3.5",
                110,
                -110,
                Status.PENDING,
                Instant.now().minus(45, ChronoUnit.MINUTES),
                // LocalDateTime.of(2022, Month.JULY, 2, 8, 15, 23),
                100.0,
                Category.NFL);
        wager2 = new Wager("Red Sox +1.5",
                72.5,
                -145,
                Status.PENDING,
                Instant.now().minus(45, ChronoUnit.MINUTES),
                // LocalDateTime.of(2022, Month.JULY, 4, 12, 15, 23),
                50.0,
                Category.MLB);

        testUser = new ApplicationUser(
            AppUserRole.ROLE_USER,
            "$2a$10$.VRp3xwjX.RTWneRZgyu3uoPKXlqsFVtvKw38u4893c83s2GazY0.",
            "test_user",
            "cash@bank.com",
            true,
            true,
            true,
            true);

        // id must be set manually
        wager1.setId(1L);
        wager2.setId(2L);

        wagerList.add(wager1);
        wagerList.add(wager2);
    }

    @AfterEach
    public void tearDown() {
        wager1 = wager2 = null;
        wagerList = null;
    }

    @Test
    public void givenWagerToAddShouldReturnAddedWager() {
        var optionalUser = Optional.of(testUser);
        when(appUserRepo.findByUsername(anyString())).thenReturn(optionalUser);
        when(wagerRepository.save(any())).thenReturn(wager1);
        wagerService.addNewWager(wager1, "username");
        verify(wagerRepository, times(1)).save(any());
    }

    @Test
    public void givenGetAllWagersShouldReturnListOfAllUsers() {
        wagerRepository.save(wager1);
        wagerRepository.save(wager2);

        when(wagerRepository.findAll()).thenReturn(wagerList);
        List<Wager> wagerList2 = wagerService.getUsersWagers("username");
        assertEquals(wagerList2, wagerList);
        verify(wagerRepository, times(1)).save(wager1);
        verify(wagerRepository, times(1)).findAll();
    }

    @Test
    public void givenIdThenShouldDeleteWager() {
        wagerRepository.save(wager1);
        Long wager1Id = wager1.getId();
        wagerService.deleteWager(wager1Id, "username");
        verify(wagerService, times(1)).deleteWager(wager1Id, "username");
        verify(wagerRepository, times(1)).save(wager1);

    }

    @Test
    public void givenIdAndStatusUpdateWager() {
        wagerRepository.save(wager1);
        Long wager1Id = wager1.getId();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("status", "WON");
        wagerService.updateWager(wager1Id, hashMap);
        verify(wagerService, times(1)).updateWager(wager1.getId(), hashMap);
    }

}
