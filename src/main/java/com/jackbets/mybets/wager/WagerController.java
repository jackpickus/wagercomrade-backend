package com.jackbets.mybets.wager;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
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
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Wager> getUsersWagers() {
        var wagers = wagerService.getUsersWagers("user");
        return wagers;
    }

    @GetMapping("/{wagerId}")
    public Wager getWager(@PathVariable("wagerId") Long wagerId) {
        var wager = wagerService.getWager(wagerId);
        return wager;
    }

    @GetMapping("/wagerlist/{category}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getWagersWithCategory(@PathVariable("category") String category, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var loggedInUser = auth.getName();

        var enumCategory = Category.valueOf(category.toUpperCase());
        var wagers = wagerService.getWagersWithCategory(loggedInUser, enumCategory);

        model.addAttribute("wagers", wagers);
        model.addAttribute("byTimePlaced", Comparator.comparing(Wager::getTimePlaced).reversed());
       
        return "list-wagers";
    }

    @PostMapping(path = "/new-wager")
    public Response placeNewWager(@RequestBody Wager wager) {
        var localDateTime = LocalDateTime.now();
        wager.setTimePlaced(localDateTime);
        wager.setStatus(Status.PENDING);
        wager.setCategory(Category.COLLEGE_BASKETBALL); // This is placeholder!!
        double toWin = wager.calcToWin(wager.getUnits(), wager.getTheOdds());
        wager.setToWin(toWin);

        return wagerService.addNewWager(wager);
    }

    // TODO Allow wagers to be deleted?
    @DeleteMapping(path = "/{wagerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWager(@PathVariable("wagerId") Long wagerId) {
        wagerService.deleteWager(wagerId);
    }

    @PutMapping(path = "/edit/{wagerId}")
    public Response updateWager(@PathVariable("wagerId") Long wagerId, @RequestBody Wager updatedWager) {
        var oldWager = wagerService.getWager(wagerId);
        var hashMap = new HashMap<String, String>();
        var oddsChanged = false;
        var unitsChanged = false;

        if (!oldWager.getTheBet().equals(updatedWager.getTheBet())) {
            hashMap.put("theBet", updatedWager.getTheBet());
        }
        if (oldWager.getTheOdds() != updatedWager.getTheOdds()) {
            oddsChanged = true;
            hashMap.put("theOdds", updatedWager.getTheOdds().toString());
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
            double toWin = updatedWager.calcToWin(updatedWager.getUnits(), updatedWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);
        } else if (oddsChanged) {
            double toWin = updatedWager.calcToWin(oldWager.getUnits(), updatedWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);
        } else if (unitsChanged) {
            double toWin = updatedWager.calcToWin(updatedWager.getUnits(), oldWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);

        }

        return wagerService.updateWager(wagerId, hashMap);
    }

    // TODO Delete this method
    @GetMapping(path = "/edit-wager/{wagerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editWager(@PathVariable("wagerId") Long wagerId, Model model) {
        var wager = wagerService.getWager(wagerId);
        model.addAttribute("wager", wager);
        return "edit-wager";
    }

}
