package com.jackbets.mybets.wager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;

@Configuration
public class WagerConfig {

    @Bean
    CommandLineRunner commandLineRunner(WagerRepository repository, ApplicationUserRepository userRepository) {

        return args -> {
            ApplicationUser myGuy = new ApplicationUser(AppUserRole.ROLE_ADMIN, "$2a$10$.VRp3xwjX.RTWneRZgyu3uoPKXlqsFVtvKw38u4893c83s2GazY0.", "admin", "cash@bank.com", true, true, true, true);
            userRepository.save(myGuy);

            Wager bet1 = new Wager(
                    "Bulls -5",
                    1.1,
                    -110,
                    Status.WON,
                    LocalDateTime.of(2022, Month.JULY, 2, 21, 30, 23),
                    1.0,
                    Category.NBA);
            Wager bet2 = new Wager(
                    "Cubs ML",
                    1,
                    200,
                    Status.LOST,
                    LocalDateTime.of(2022, Month.JULY, 2, 8, 15, 23),
                    2.0,
                    Category.MLB);
            Wager bet3 = new Wager(
                    "Oakland A\'s ML",
                    0.65,
                    154,
                    Status.WON,
                    LocalDateTime.of(2022, Month.JULY, 11, 8, 15, 23),
                    1.0,
                    Category.MLB);
            Wager bet4 = new Wager(
                    "Tennessee/Missouri O69",
                    2.0,
                    100,
                    Status.PENDING,
                    LocalDateTime.of(2022, Month.NOVEMBER, 13, 16, 43, 34),
                    2.0,
                    Category.NFL);

            bet1.setUser(myGuy);
            bet2.setUser(myGuy);
            bet3.setUser(myGuy);
            bet4.setUser(myGuy);

            repository.saveAll(List.of(bet1, bet2, bet3, bet4));

        };
    }

}
