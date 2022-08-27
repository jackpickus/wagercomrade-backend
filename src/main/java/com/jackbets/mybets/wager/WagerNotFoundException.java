package com.jackbets.mybets.wager;

public class WagerNotFoundException extends RuntimeException {

    public WagerNotFoundException(Long id) {
        super("Could not find wager " + id);
    }

}
