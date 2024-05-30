package com.jackbets.mybets.wager;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
                    new BigDecimal(1.1),
                    -110,
                    Status.WON,
                    Instant.now().minus(6, ChronoUnit.HOURS),
                    new BigDecimal(1.0),
                    Category.NBA);
            Wager bet2 = new Wager(
                    "Cubs ML",
                    new BigDecimal(1),
                    200,
                    Status.LOST,
                    Instant.now().minus(4, ChronoUnit.HOURS),
                    new BigDecimal(2.0),
                    Category.MLB);
            Wager bet3 = new Wager(
                    "Oakland A\'s ML",
                    new BigDecimal(0.65),
                    154,
                    Status.VOID,
                    Instant.now().minus(2, ChronoUnit.HOURS),
                    new BigDecimal(1.0),
                    Category.MLB);
            Wager bet4 = new Wager(
                    "Tennessee/Missouri O69",
                    new BigDecimal(2.0),
                    100,
                    Status.PENDING,
                    Instant.now(),
                    new BigDecimal(2.0),
                    Category.CFB);

            bet1.setUser(myGuy);
            bet2.setUser(myGuy);
            bet3.setUser(myGuy);
            bet4.setUser(myGuy);

            repository.saveAll(List.of(bet1, bet2, bet3, bet4));

        };
    }

}
