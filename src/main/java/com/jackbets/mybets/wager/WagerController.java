package com.jackbets.mybets.wager;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jackbets.mybets.response.Response;
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

    @DeleteMapping(path = "api/v1/wager/{wagerId}")
    @PreAuthorize("hasAuthority('bet:write')")
    public void deleteWager(@PathVariable("wagerId") Long wagerId) {
        wagerService.deleteWager(wagerId);
    }

    @PutMapping(path = "api/v1/wager/{wagerId}")
    @PreAuthorize("hasAuthority('bet:write')")
    public void updateWager(@PathVariable("wagerId") Long wagerId, @RequestParam("status") Status status) {
        wagerService.updateWager(wagerId, status);
    }

}
