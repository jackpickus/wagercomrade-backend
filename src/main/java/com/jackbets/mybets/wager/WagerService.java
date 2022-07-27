package com.jackbets.mybets.wager;

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

    public void deleteWager(Long wagerId) {
        boolean exists = wagerRepository.existsById(wagerId);
        if (!exists) {
            throw new WagerNotFoundException(wagerId);
        }
        wagerRepository.deleteById(wagerId);
    }

    @Transactional
    public void updateWager(Long wagerId, Status status) {
        Wager wager = wagerRepository.findById(wagerId)
                .orElseThrow(() -> new WagerNotFoundException(wagerId));

        if (status instanceof Status) {
            wager.setStatus(status);
        }
    }

}
