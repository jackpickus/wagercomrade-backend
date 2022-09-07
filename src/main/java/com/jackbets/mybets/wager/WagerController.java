package com.jackbets.mybets.wager;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/wager")
public class WagerController {

    private final WagerService wagerService;

    public WagerController(WagerService wagerService) {
        this.wagerService = wagerService;
    }

    @GetMapping
    public List<Wager> getWagers() {
        return wagerService.getWagers();
    }

    @PostMapping("management")
    public Response placeNewWager(@RequestBody Wager wager) {
        return wagerService.addNewWager(wager);
    }

    @DeleteMapping("management/{wagerId}")
    public void deleteWager(@PathVariable("wagerId") Long wagerId) {
        wagerService.deleteWager(wagerId);
    }

    @PutMapping(path = "management/{wagerId}")
    public void updateWager(@PathVariable("wagerId") Long wagerId, @RequestParam("status") Status status) {
        wagerService.updateWager(wagerId, status);
    }

}
