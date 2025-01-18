package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.InsufficientAdminPrivilegesException;
import com.javaeducase.ecommerce.exception.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exception.user.UserNotFoundException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import com.javaeducase.ecommerce.util.user.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userRepository.findAll().stream()
                .map(UserUtils::convertToDTO)
                .collect(Collectors.toList());
        log.info("Successfully fetched {} users", users.size());
        return users;
    }

    public UserDTO getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = getUserByIdWithCheck(id);
        log.info("User with id: {} found", id);
        return UserUtils.convertToDTO(user);
    }

    public void changeUserPassword(Long id, ChangePasswordRequestDTO request) {
        log.info("Changing password for user with id: {}", id);
        User user = getUserByIdWithCheck(id);
        if (user.isDeleted()) {
            log.error("User with id: {} is deleted", id);
            throw new UserIsDeletedException("User is deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            log.error("Admin with id: {} cannot change password of another admin", id);
            throw new InsufficientAdminPrivilegesException("Admin cannot change data of another admin");
        }

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        log.info("Validating old password for user with id: {}", id);
        UserUtils.checkPasswords(oldPassword, newPassword, user.getPassword(), passwordEncoder);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password change successful for user with id: {}", id);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);

        User user = getUserByIdWithCheck(id);
        if (user.isDeleted()) {
            log.error("User with id: {} is deleted", id);
            throw new UserIsDeletedException("User is deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            log.error("Admin with id: {} cannot update data of another admin", id);
            throw new InsufficientAdminPrivilegesException("Admin cannot change data of another admin");
        }

        log.info("Validating email for user with id: {}", id);
        UserUtils.validateEmail(userDTO.getEmail());
        UserUtils.checkEmailExists(userDTO.getEmail(), userRepository);
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        User updatedUser = userRepository.save(user);
        log.info("User with id: {} successfully updated", id);
        return UserUtils.convertToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        User user = getUserByIdWithCheck(id);
        if (user.isDeleted()) {
            log.error("User with id: {} is already deleted", id);
            throw new UserIsDeletedException("User is deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            log.error("Admin with id: {} cannot delete another admin", id);
            throw new InsufficientAdminPrivilegesException("Admin cannot delete another admin");
        }

        user.setDeleted(true);
        userRepository.save(user);
        log.info("User with id: {} successfully deleted", id);
    }

    private User getUserByIdWithCheck(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
    }
}
