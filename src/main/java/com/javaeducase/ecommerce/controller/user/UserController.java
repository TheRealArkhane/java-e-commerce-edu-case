package com.javaeducase.ecommerce.controller.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.service.user.UserService;
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
    

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        UserDTO userDTO = userService.getUserByEmail(currentUserEmail);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateCurrentUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteCurrentUser() {
        userService.deleteCurrentUser();
        SecurityContextHolder.getContext().setAuthentication(null);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Пользователь успешно удален");
        return new ResponseEntity<>(responseBody, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/me/change_password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        userService.changePassword(request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Пароль успешно изменен");
        return ResponseEntity.ok(responseBody);
    }
}