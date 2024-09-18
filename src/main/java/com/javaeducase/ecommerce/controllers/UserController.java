package com.javaeducase.ecommerce.controllers;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.services.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Получаем email текущего пользователя
        UserDTO userDTO = userService.getUserByEmail(currentUserEmail);
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

    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteCurrentUser(HttpServletResponse response) {
        userService.deleteCurrentUser();
        SecurityContextHolder.getContext().setAuthentication(null);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User successfully deleted");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/me/change_password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request.getOldPassword(), request.getNewPassword(), passwordEncoder);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Пароль успешно изменен");
        return ResponseEntity.ok(responseBody);
    }

    @Getter
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}