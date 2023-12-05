package com.jackbets.mybets.wager;

import java.util.HashMap;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;

@Service
public class WagerService {

    private final WagerRepository wagerRepository;
    private final ApplicationUserRepository appUserRepository;

    public WagerService(WagerRepository wagerRepository,
        ApplicationUserRepository appUserReppsitory) {

        this.wagerRepository = wagerRepository;
        this.appUserRepository = appUserReppsitory;
    }

    public List<Wager> getUsersWagers(String username) {
        // ApplicationUser appUser = appUserRepository.findByUsername(username)
        //     .orElseThrow(() -> new IllegalArgumentException()); 

        // var wagers = appUserRepository.getUsersWagers(appUser); 
        return wagerRepository.findAll();
    }

    public List<Wager> getWagersWithCategory(String username, Category category) {
        ApplicationUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException()); 

        var wagers = appUserRepository.getUsersWagersWithCategory(appUser, category); 
        return wagers;

    }

    public Response addNewWager(Wager wager) {
        System.out.println("New wager\'s date: " + wager.getTimePlaced());

        var appUser = appUserRepository.getReferenceById(1L); // This is dummy data and must be removed!!

        wager.setUser(appUser);
        Wager newWager = wagerRepository.save(wager);

        return new Response(newWager.getId());
    }

    public void deleteWager(Long wagerId) {
        boolean exists = wagerRepository.existsById(wagerId);
        if (!exists) {
            throw new WagerNotFoundException(wagerId);
        }
        wagerRepository.deleteById(wagerId);
    }

    @Transactional
    public Response updateWager(Long wagerId, HashMap<String, String> hmap) {
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

                case "category":
                    Category newCategory = Category.valueOf(value);
                    wager.setCategory(newCategory);
                    break;

                case "toWin":
                    Double newToWin = Double.parseDouble(value);
                    wager.setToWin(newToWin);
                    break;
            }
        });

        return new Response(wager.getId());
    }

    public Wager getWager(Long wagerId) {
        Wager wager = wagerRepository.findById(wagerId)
                .orElseThrow(() -> new WagerNotFoundException(wagerId));
        return wager;
    }

}
