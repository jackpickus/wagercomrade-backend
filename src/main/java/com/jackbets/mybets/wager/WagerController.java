package com.jackbets.mybets.wager;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;

@RestController
public class WagerController {

    private final WagerService wagerService;

    public WagerController(WagerService wagerService) {
        this.wagerService = wagerService;
    }

    @GetMapping({"/wagerlist", "/"})
    public ModelAndView getAllWagers() {
        List<Wager> wagers = wagerService.getWagers();
        ModelAndView modelAndView = new ModelAndView("list-wagers");
        modelAndView.addObject("wagers", wagers);
        return modelAndView;
    }

    @PostMapping(path = "api/v1/wager")
    @PreAuthorize("hasAuthority('bet:write')")
    public Response placeNewWager(@RequestBody Wager wager) {
        return wagerService.addNewWager(wager);
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
