package com.jackbets.mybets.wager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
    public List<WagerResponse> getUsersWagers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var wagers = wagerService.getUsersWagers(loggedInUser);
        List<WagerResponse> wagerResponse = new ArrayList<>();
        for (Wager ogWager : wagers) {
            var response = WagerResponse.builder()
                .id(ogWager.getId())
                .theBet(ogWager.getTheBet())
                .category(ogWager.getCategory())
                .theOdds(ogWager.getTheOdds())
                .units(ogWager.getUnits())
                .status(ogWager.getStatus())
                .timePlaced(ogWager.getTimePlaced())
                .toWin(ogWager.getToWin())
                .build();
            wagerResponse.add(response);
        }
        return wagerResponse;
    }

    @GetMapping("/{wagerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public WagerResponse getWager(@PathVariable("wagerId") Long wagerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var ogWager = wagerService.getWager(wagerId, loggedInUser);
        var wagerResponse = WagerResponse.builder()
                .id(ogWager.getId())
                .theBet(ogWager.getTheBet())
                .category(ogWager.getCategory())
                .theOdds(ogWager.getTheOdds())
                .units(ogWager.getUnits())
                .status(ogWager.getStatus())
                .timePlaced(ogWager.getTimePlaced())
                .toWin(ogWager.getToWin())
                .build();
        return wagerResponse;
    }

    @GetMapping("/wagerlist/{category}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<WagerResponse> getWagersWithCategory(@PathVariable("category") String category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();
        var enumCategory = Category.valueOf(category.toUpperCase());
        var wagers = wagerService.getWagersWithCategory(loggedInUser, enumCategory);
        List<WagerResponse> wagerResponse = new ArrayList<>();
        for (Wager ogWager : wagers) {
            var response = WagerResponse.builder()
                .id(ogWager.getId())
                .theBet(ogWager.getTheBet())
                .category(ogWager.getCategory())
                .theOdds(ogWager.getTheOdds())
                .units(ogWager.getUnits())
                .status(ogWager.getStatus())
                .timePlaced(ogWager.getTimePlaced())
                .toWin(ogWager.getToWin())
                .build();
            wagerResponse.add(response);
        }
        return wagerResponse;
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
