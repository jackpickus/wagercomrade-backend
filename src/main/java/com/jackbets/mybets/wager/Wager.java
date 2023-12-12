package com.jackbets.mybets.wager;

import java.time.LocalDateTime;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table
@Slf4j
@EqualsAndHashCode
public class Wager {

    @Id
    @SequenceGenerator(name = "wager_sequence", sequenceName = "wager_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wager_sequence")
    private @Getter @Setter Long id;
    private @Getter @Setter String theBet;
    private @Getter @Setter double units;
    private @Getter @Setter Integer theOdds;
    private @Getter @Setter Status status;
    private @Getter @Setter LocalDateTime timePlaced;
    private @Getter @Setter Double toWin;
    private @Getter @Setter Category category;

    @Setter
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;

    public Wager() {
    }

    public Wager(String theBet, double units, Integer theOdds, Status status, LocalDateTime timePlaced,
            Double toWin, Category category) {
        this.theBet = theBet;
        this.units = units;
        this.theOdds = theOdds;
        this.status = status;
        this.timePlaced = timePlaced;
        this.toWin = toWin;
        this.category = category;
    }

    double calcToWin(double units, int odds) {
        double amount;
        if (units < 0.1) {
            log.error("units must be greater than 0.1");
            return -1;
        } else if (odds < 100 && odds > -100) {
            log.error("odds must be greater than 99 or less than -100");
            return -1;
        }
    
        if (odds >= 100) {
            // wager was on underdog
            amount = units * (odds / 100.0);
        } else {
            // wager was negative odds, chose favorite
            amount = units * (100.0 / (odds * -1.0));
        }
        double roundedAmount = Math.round(amount * 100.0) / 100.0;
        log.info("Risking " + units + " to win " + amount);
        log.info("Odds: " + odds);
        return roundedAmount;
    }

}
