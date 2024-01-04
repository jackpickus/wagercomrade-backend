package com.jackbets.mybets.registration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegistrationRequest request
        ) {
            return ResponseEntity.ok(registrationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
        ) {
            return ResponseEntity.ok(registrationService.authenticate(request));
    }

    @GetMapping("/confirm")
    public String getMethodName(@RequestParam("token") String confirmationToken) {
        return registrationService.confirmToken(confirmationToken);
    }
    
    
    
}
