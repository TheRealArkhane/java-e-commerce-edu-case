package com.javaeducase.ecommerce.controllers.user;

import com.javaeducase.ecommerce.dto.user.RegistrationDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.services.user.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationAuthorizationController {

    private final RegistrationService registrationService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        UserDTO newUser = registrationService.registerUser(registrationDTO, passwordEncoder);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
