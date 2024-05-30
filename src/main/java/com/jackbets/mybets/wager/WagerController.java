package com.jackbets.mybets.wager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping(path = "api/v1/wager")
public class WagerController {

    private final WagerService wagerService;

    public WagerController(WagerService wagerService) {
        this.wagerService = wagerService;
    }

    @GetMapping({"/wagerlist", "/"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Wager> getUsersWagers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var wagers = wagerService.getUsersWagers(loggedInUser);
        return wagers;
    }

    @GetMapping("/{wagerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Wager getWager(@PathVariable("wagerId") Long wagerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var wager = wagerService.getWager(wagerId, loggedInUser);
        return wager;
    }

    @GetMapping("/wagerlist/{category}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Wager> getWagersWithCategory(@PathVariable("category") String category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var enumCategory = Category.valueOf(category.toUpperCase());
        var wagers = wagerService.getWagersWithCategory(loggedInUser, enumCategory);
        return wagers;
    }

    @PostMapping(path = "/new-wager")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Response placeNewWager(@RequestBody Wager wager) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();

        var wagerTimeStamp = Instant.now();
        wager.setTimePlaced(wagerTimeStamp);
        wager.setStatus(Status.PENDING);
        BigDecimal toWin = wager.calcToWin(wager.getUnits(), wager.getTheOdds());
        wager.setToWin(toWin);

        return wagerService.addNewWager(wager, loggedInUser);
    }

    @DeleteMapping(path = "/{wagerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteWager(@PathVariable("wagerId") Long wagerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();

        wagerService.deleteWager(wagerId, loggedInUser);
    }

    @PutMapping(path = "/edit/{wagerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Response updateWager(@PathVariable("wagerId") Long wagerId, @RequestBody Wager updatedWager) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var oldWager = wagerService.getWager(wagerId, loggedInUser);
        var hashMap = new HashMap<String, String>();
        var oddsChanged = false;
        var unitsChanged = false;

        if (!oldWager.getTheBet().equals(updatedWager.getTheBet())) {
            hashMap.put("theBet", updatedWager.getTheBet());
        }
        if (oldWager.getTheOdds() != updatedWager.getTheOdds()) {
            oddsChanged = true;
            hashMap.put("theOdds", String.valueOf(updatedWager.getTheOdds()));
        }
        if (oldWager.getUnits() != updatedWager.getUnits()) {
            unitsChanged = true;
            String unitsString = String.valueOf(updatedWager.getUnits());
            hashMap.put("units", unitsString);
        }
        if (!oldWager.getStatus().equals(updatedWager.getStatus())) {
            hashMap.put("status", updatedWager.getStatus().toString());
        }
        if (!oldWager.getCategory().equals(updatedWager.getCategory())) {
            hashMap.put("category", updatedWager.getCategory().toString());
        }
        if (oddsChanged && unitsChanged) {
            BigDecimal toWin = updatedWager.calcToWin(updatedWager.getUnits(), updatedWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);
        } else if (oddsChanged) {
            BigDecimal toWin = updatedWager.calcToWin(oldWager.getUnits(), updatedWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);
        } else if (unitsChanged) {
            BigDecimal toWin = updatedWager.calcToWin(updatedWager.getUnits(), oldWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);

        }

        return wagerService.updateWager(wagerId, hashMap);
    }

}
