package com.javaeducase.ecommerce.controller.user;

import com.javaeducase.ecommerce.dto.user.RegistrationDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.user.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Register",
        description = "Methods for register a new user with email validation")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationAuthorizationController {

    private final RegistrationService registrationService;


    @Operation(summary = "Register a new user",
            description = "Register a new user in the system with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "User successfully registered",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid registration details or validation error",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        UserDTO newUser = registrationService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
