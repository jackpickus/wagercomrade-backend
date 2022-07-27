package com.jackbets.mybets.wager;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jackbets.mybets.status.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
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
    private @Getter @Setter Integer toWin;

    public Wager() {
    }

    public Wager(String theBet, double units, Integer theOdds, Status status, LocalDateTime timePlaced,
            Integer toWin) {
        this.theBet = theBet;
        this.units = units;
        this.theOdds = theOdds;
        this.status = status;
        this.timePlaced = timePlaced;
        this.toWin = toWin;
    }

}
