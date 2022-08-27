package com.jackbets.mybets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.jackbets.mybets.status.Status;
import com.jackbets.mybets.wager.Wager;
import com.jackbets.mybets.wager.WagerNotFoundException;
import com.jackbets.mybets.wager.WagerRepository;
import com.jackbets.mybets.wager.WagerService;

@ExtendWith(MockitoExtension.class)
class WagerServiceTest {

    @Mock
    private WagerRepository wagerRepository;

    @InjectMocks
    private WagerService wagerService;

    @Mock
    private WagerService wagerService2;

    private Wager wager1;
    private Wager wager2;
    List<Wager> wagerList;

    @BeforeEach
    public void setUp() {
        wagerList = new ArrayList<>();
        wager1 = new Wager("Bears +3.5",
                110,
                -110,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 2, 8, 15, 23),
                100);
        wager2 = new Wager("Red Sox +1.5",
                72.5,
                -145,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 4, 12, 15, 23),
                50);

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
        when(wagerRepository.save(any())).thenReturn(wager1);
        wagerService.addNewWager(wager1);
        verify(wagerRepository, times(1)).save(any());
    }

    @Test
    public void givenGetAllWagersShouldReturnListOfAllUsers() {
        wagerRepository.save(wager1);
        wagerRepository.save(wager2);

        when(wagerRepository.findAll()).thenReturn(wagerList);
        List<Wager> wagerList2 = wagerService.getWagers();
        assertEquals(wagerList2, wagerList);
        verify(wagerRepository, times(1)).save(wager1);
        verify(wagerRepository, times(1)).findAll();
    }

    @Test
    public void givenIdThenShouldDeleteWager() {
        wagerRepository.save(wager1);
        Long wager1Id = wager1.getId();
        wagerService2.deleteWager(wager1Id);
        verify(wagerService2, times(1)).deleteWager(wager1Id);
        verify(wagerRepository, times(1)).save(wager1);

    }


}
