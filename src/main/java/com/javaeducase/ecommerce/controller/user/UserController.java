package com.javaeducase.ecommerce.controller.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.handler.GlobalExceptionHandler;
import com.javaeducase.ecommerce.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    

    @Operation (summary = "Get logged-in user",
            description = "Get user that currently logged-in")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200",
                    content = { @Content (
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse (responseCode = "404",
                    description = "User not found",
                    content = { @Content (
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse (responseCode = "403",
                    description = "User is deleted",
                    content = { @Content (
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        UserDTO userDTO = userService.getUserByEmail(currentUserEmail);
        return ResponseEntity.ok(userDTO);
    }


    @Operation(summary = "Update logged-in user",
            description = "Update the information of the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "403",
                    description = "User is deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateCurrentUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    @Operation(summary = "Delete logged-in user",
            description = "Delete the account of the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "User successfully deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "403",
                    description = "User is deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteCurrentUser() {
        userService.deleteCurrentUser();
        SecurityContextHolder.getContext().setAuthentication(null);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User successfully deleted");
        return new ResponseEntity<>(responseBody, HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Change password",
            description = "Change the password of the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Password successfully changed",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid password or validation error",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "403",
                    description = "User is deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PutMapping("/me/change_password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        userService.changePassword(request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }
}