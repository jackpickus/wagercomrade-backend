package com.jackbets.mybets.wager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
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
    private @Getter @Setter BigDecimal units;
    private @Getter @Setter int theOdds;
    private @Getter @Setter Status status;
    private @Getter @Setter Instant timePlaced;
    private @Getter @Setter BigDecimal toWin;
    private @Getter @Setter Category category;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;

    public Wager() {
    }

    public Wager(String theBet, BigDecimal units, Integer theOdds, Status status, Instant timePlaced,
            BigDecimal toWin, Category category) {
        this.theBet = theBet;
        this.units = units;
        this.theOdds = theOdds;
        this.status = status;
        this.timePlaced = timePlaced;
        this.toWin = toWin;
        this.category = category;
    }

    BigDecimal calcToWin(BigDecimal units, int odds) {
        BigDecimal amount;
        BigDecimal dime = new BigDecimal(0.1);
        if (units.compareTo(dime) < 0) {
            log.error("units must be greater than 0.1");
            return new BigDecimal(-1);
        } else if (odds < 100 && odds > -100) {
            log.error("odds must be greater than 99 or less than -100");
            return new BigDecimal(-1);
        }
    
        BigDecimal bdOdds = new BigDecimal(odds);
        BigDecimal oneH = new BigDecimal(100.0);
        if (odds >= 100) {
            // wager was on underdog
            BigDecimal multiplier = bdOdds.divide(oneH);
            amount = units.multiply(multiplier);
        } else {
            // wager was negative odds, chose favorite
            BigDecimal negOne = new BigDecimal(-1.0);
            BigDecimal multiplier1 = bdOdds.multiply(negOne);
            BigDecimal divide = oneH.divide(multiplier1);
            amount = units.multiply(divide);
        }
        BigDecimal roundedAmount = amount
            .round(new MathContext(2, RoundingMode.HALF_EVEN));
        log.info("Risking " + units + " to win " + amount);
        log.info("Odds: " + odds);
        return roundedAmount;
    }

}
