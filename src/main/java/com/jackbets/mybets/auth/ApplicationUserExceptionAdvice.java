package com.jackbets.mybets.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ApplicationUserExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String applicationUserExceptionHandler(UsernameExistsException ex) {
        return ex.getMessage();
    }
    
}
