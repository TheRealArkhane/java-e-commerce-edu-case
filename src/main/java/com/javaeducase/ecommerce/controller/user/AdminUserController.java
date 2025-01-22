package com.javaeducase.ecommerce.controller.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.service.user.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = adminUserService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {

        adminUserService.deleteUser(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User successfully deleted");
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/{id}/change_password")
    public ResponseEntity<Map<String, String>> changeUserPassword(@PathVariable Long id,
                                                                  @RequestBody ChangePasswordRequestDTO request) {
        adminUserService.changeUserPassword(id, request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }
}
