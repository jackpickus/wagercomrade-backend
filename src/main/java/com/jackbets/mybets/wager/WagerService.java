package com.jackbets.mybets.wager;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jackbets.mybets.auth.AppUserRole;
import com.jackbets.mybets.auth.ApplicationUser;
import com.jackbets.mybets.auth.ApplicationUserRepository;
import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;

import jakarta.transaction.Transactional;

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
        ApplicationUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException()); 

        var wagers = appUserRepository.getUsersWagers(appUser); 
        return wagers;
    }

    public List<Wager> getWagersWithCategory(String username, Category category) {
        ApplicationUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException()); 

        var wagers = appUserRepository.getUsersWagersWithCategory(appUser, category); 
        return wagers;

    }

    public Response addNewWager(Wager wager, String username) {
        ApplicationUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException());

        wager.setUser(appUser);
        Wager newWager = wagerRepository.save(wager);

        return new Response(newWager.getId(), "Wager created successfully", newWager.getTimePlaced());
    }

    public void deleteWager(Long wagerId, String username) {
        ApplicationUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException());

        boolean exists = wagerRepository.existsById(wagerId);
        if (!exists) {
            throw new WagerNotFoundException(wagerId);
        }

        var wagerOptional = wagerRepository.findById(wagerId);
        var wagersUser = wagerOptional.get().getUser();
        if (Objects.equals(wagersUser.getId(), appUser.getId()) || appUser.getAppUserRole().equals(AppUserRole.ROLE_ADMIN)) {
            wagerRepository.deleteById(wagerId);
        }
        throw new IllegalStateException("Not users wager to delete!");
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

        return new Response(wager.getId(), "Wager updated successfully", wager.getTimePlaced());
    }

    public Wager getWager(Long wagerId, String username) {
        ApplicationUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format("Username \'%s\' not found", username))
            ); 

        Wager wager = wagerRepository.findById(wagerId)
                .orElseThrow(() -> new WagerNotFoundException(wagerId));
        if (Objects.equals(wager.getId(), appUser.getId()) || appUser.getAppUserRole().equals(AppUserRole.ROLE_ADMIN)) {
            return wager;
        }

        throw new IllegalStateException("Not users wager!");
    }

}
