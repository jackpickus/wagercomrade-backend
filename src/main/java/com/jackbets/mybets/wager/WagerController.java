package com.jackbets.mybets.wager;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.jackbets.mybets.status.Status;

@Controller
public class WagerController {

    private final WagerService wagerService;

    public WagerController(WagerService wagerService) {
        this.wagerService = wagerService;
    }

    @GetMapping({"/wagerlist", "/"})
    public String getAllWagers(Model model) {
        List<Wager> wagers = wagerService.getWagers();
        model.addAttribute("wagers", wagers);
        model.addAttribute("byTimePlaced", Comparator.comparing(Wager::getTimePlaced).reversed());
        return "list-wagers";
    }

    @GetMapping("/wager/{wagerId}")
    public String getWager(@PathVariable("wagerId") Long wagerId, Model model) {
        Wager wager = wagerService.getWager(wagerId);
        model.addAttribute("wager", wager);
        return "wager";
    }

    @GetMapping("/new-wager")
    @PreAuthorize("hasAuthority('bet:write')")
    public String newWagerForm(Model model) {
        model.addAttribute("new_wager", new Wager());
        return "new-wager";
    }

    @PostMapping(path = "/new-wager")
    @PreAuthorize("hasAuthority('bet:write')")
    public String placeNewWager(@ModelAttribute Wager wager, Model model) {
        model.addAttribute("new_wager", wager);
        LocalDateTime localDateTime = LocalDateTime.now();
        wager.setTimePlaced(localDateTime);
        wager.setStatus(Status.PENDING);
        double toWin = wager.calcToWin(wager.getUnits(), wager.getTheOdds());
        wager.setToWin(toWin);
        wagerService.addNewWager(wager);
        return "redirect:/wagerlist";
    }

    // TODO Allow wagers to be deleted?
    @DeleteMapping(path = "api/v1/wager/{wagerId}")
    @PreAuthorize("hasAuthority('bet:write')")
    public void deleteWager(@PathVariable("wagerId") Long wagerId) {
        wagerService.deleteWager(wagerId);
    }

    @PostMapping(path = "/wager/{wagerId}")
    @PreAuthorize("hasAuthority('bet:write')")
    public String updateWager(@PathVariable("wagerId") Long wagerId, @ModelAttribute Wager updatedWager, Model model) {
        model.addAttribute("wager", updatedWager);
        Wager oldWager = wagerService.getWager(wagerId);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Boolean oddsChanged = false;
        Boolean unitsChanged = false;
        if (updatedWager.equals(oldWager)) {
            return "redirect:/wagerlist";
        }
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
        if (oddsChanged || unitsChanged) {
            double toWin = updatedWager.calcToWin(updatedWager.getUnits(), updatedWager.getTheOdds());
            String toWinString = String.valueOf(toWin);
            hashMap.put("toWin", toWinString);
        }
        wagerService.updateWager(wagerId, hashMap);
        return "redirect:/wager/{wagerId}";
    }

    @GetMapping(path = "/edit-wager/{wagerId}")
    @PreAuthorize("hasAuthority('bet:write')")
    public String editWager(@PathVariable("wagerId") Long wagerId, Model model) {
        Wager wager = wagerService.getWager(wagerId);
        model.addAttribute("wager", wager);
        return "edit-wager";
    }

}
