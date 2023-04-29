package com.jackbets.mybets.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;
import com.jackbets.mybets.wager.Wager;
import com.jackbets.mybets.wager.WagerRepository;

// @DataJpaTest
@SpringBootTest
class WagerRepositoryTest {

    @Autowired
    private WagerRepository wagerRepository;
    private Wager wager;

    @BeforeEach
    public void setUp() {
        Set<Category> footballSet = new HashSet<Category>();
        footballSet.add(Category.NFL);
        wager = new Wager(
                "Bears +3.5",
                110,
                -110,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 2, 8, 15, 23),
                100.0,
                footballSet);
    }

    @AfterEach
    public void tearDown() {
        wagerRepository.deleteAll();
        wager = null;
    }

    @Test
    public void givenWagerToAddShouldReturnAddedWager() {
        wagerRepository.save(wager);
        Wager fetchedWager = wagerRepository.findById(wager.getId()).get();

        assertEquals(1, fetchedWager.getId());
    }

    @Test
    public void givenGetAllWagersShouldReturnListOfAllWagers() {
        Set<Category> baseballSet = new HashSet<Category>();
        baseballSet.add(Category.MLB);
        Wager wager1 = new Wager(
                "Texas Rangers -1.5",
                100,
                145,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 4, 8, 15, 23),
                145.0,
                baseballSet);
        Wager wager2 = new Wager(
                "Cubs +1.5",
                145,
                -145,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 4, 12, 15, 23),
                100.0,
                baseballSet);

        wagerRepository.save(wager1);
        wagerRepository.save(wager2);

        List<Wager> wagerList = (List<Wager>) wagerRepository.findAll();
        assertEquals("Texas Rangers -1.5", wagerList.get(0).getTheBet());
        assertEquals("Cubs +1.5", wagerList.get(1).getTheBet());
    }

    @Test
    public void givenIdThenShouldReturnWagerOfThatId() {
        Set<Category> baseballSet = new HashSet<Category>();
        baseballSet.add(Category.MLB);
        Wager wager1 = new Wager(
                "Cubs +1.5",
                72.5,
                -145,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 4, 12, 15, 23),
                50.0,
                baseballSet);

        Wager wager2 = wagerRepository.save(wager1);

        Optional<Wager> optional = wagerRepository.findById(wager2.getId());
        assertEquals(wager2.getId(), optional.get().getId());
        assertEquals(wager2.getTheBet(), optional.get().getTheBet());
    }

    @Test
    public void givenIdToDeleteThenShouldDeleteWager() {
        Set<Category> baseballSet = new HashSet<Category>();
        baseballSet.add(Category.MLB);
        Wager wager = new Wager(
                "Red Sox +1.5",
                72.5,
                -145,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 4, 12, 15, 23),
                50.0,
                baseballSet);

        wagerRepository.save(wager);
        wagerRepository.deleteById(wager.getId());
        Optional<Wager> optional = wagerRepository.findById(wager.getId());
        assertEquals(Optional.empty(), optional);

    }

    @Test
    public void givenFiveWagersReturnCountOfFive() {
        int count = 0;
        Set<Category> baseballSet = new HashSet<Category>();
        baseballSet.add(Category.MLB);
        while (count < 5) {
            Wager wager = new Wager(
                    "Red Sox +1.5",
                    72.5,
                    -145,
                    Status.PENDING,
                    LocalDateTime.of(2022, Month.JULY, 4, count, 15, 23),
                    50.0,
                    baseballSet);

            wagerRepository.save(wager);
            count++;
        }

        Long wagerRepoCount = wagerRepository.count();
        assertEquals(5L, wagerRepoCount);
    }

}
