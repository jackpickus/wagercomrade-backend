package com.jackbets.mybets.registration.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConfirmationTokenExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ConfirmationTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String confirmationTokenExpiredHandler(ConfirmationTokenException ex) {
        return ex.getMessage();
    }
    
}
