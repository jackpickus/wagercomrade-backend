package com.jackbets.mybets.wager;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class WagerNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(WagerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String wagerNotFoundHandler(WagerNotFoundException ex) {
        return ex.getMessage();
    }

}
