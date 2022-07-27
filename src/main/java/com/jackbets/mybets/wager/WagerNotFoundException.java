package com.jackbets.mybets.wager;

public class WagerNotFoundException extends RuntimeException {

    WagerNotFoundException(Long id) {
        super("Could not find wager " + id);
    }

}
