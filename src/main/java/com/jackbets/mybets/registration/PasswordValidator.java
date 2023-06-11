package com.jackbets.mybets.registration;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidator implements Predicate<String>{

    @Override
    public boolean test(String t) {
    
        var regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
        return Pattern.compile(regexPattern).matcher(t).matches();
    }
    
}
