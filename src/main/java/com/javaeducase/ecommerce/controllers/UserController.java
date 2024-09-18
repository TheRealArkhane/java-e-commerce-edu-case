package com.javaeducase.ecommerce.controllers;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO userDTO = userService.getUserById(userService.getCurrentUser().getId());
        if (userDTO.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateCurrentUser(userDTO);
        if (updatedUser.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/me/change_password")
    public ResponseEntity<Void> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(HttpServletResponse response) {
        userService.deleteCurrentUser();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO, @RequestParam String password) {
        UserDTO newUser = userService.registerUser(userDTO, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}