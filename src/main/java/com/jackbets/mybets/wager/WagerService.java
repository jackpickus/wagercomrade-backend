package com.jackbets.mybets.wager;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;

@Service
public class WagerService {

    private final WagerRepository wagerRepository;

    public WagerService(WagerRepository wagerRepository) {
        this.wagerRepository = wagerRepository;
    }

    public List<Wager> getWagers() {
        return wagerRepository.findAll();
    }

    public Response addNewWager(Wager wager) {
        System.out.println("New wager\'s date: " + wager.getTimePlaced());
        Wager newWager = wagerRepository.save(wager);
        return new Response(newWager.getId());
    }

    // TODO Allow wagers to be deleted?
    public void deleteWager(Long wagerId) {
        boolean exists = wagerRepository.existsById(wagerId);
        if (!exists) {
            throw new WagerNotFoundException(wagerId);
        }
        wagerRepository.deleteById(wagerId);
    }

    @Transactional
    public void updateWager(Long wagerId, HashMap<String, String> hmap) {
        Wager wager = wagerRepository.findById(wagerId)
                .orElseThrow(() -> new WagerNotFoundException(wagerId));

        hmap.forEach((key, value) -> {
            // ? Should a switch statement be used here
            switch (key) {
                case "theBet":
                    wager.setTheBet(value);
                    break;

                case "theOdds":
                    int newOdds = Integer.valueOf(value);
                    wager.setTheOdds(newOdds);
                    break;

                case "units":
                    Double newUnits = Double.parseDouble(value);
                    wager.setUnits(newUnits);
                    break;

                case "status":
                    Status newStatus = Status.valueOf(value);
                    wager.setStatus(newStatus);
                    break;

                case "toWin":
                    Double newToWin = Double.parseDouble(value);
                    wager.setToWin(newToWin);
                    break;
            }
        });
    }

    public Wager getWager(Long wagerId) {
        Wager wager = wagerRepository.findById(wagerId)
                .orElseThrow(() -> new WagerNotFoundException(wagerId));
        return wager;
    }

}
