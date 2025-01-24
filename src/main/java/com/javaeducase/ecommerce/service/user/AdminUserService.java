package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.ChangeUserDataRequestDTO;
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
        log.info("Fetching all users...");
        List<UserDTO> users = userRepository.findAll().stream()
                .map(UserUtils::convertUserToUserDTO)
                .collect(Collectors.toList());
        log.info("Successfully fetched {} users", users.size());
        return users;
    }

    public UserDTO getUserById(Long id) {
        log.info("Fetching user with id: {}...", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        log.info("User with id: {} found", id);
        return UserUtils.convertUserToUserDTO(user);
    }

    public void changeUserPassword(Long id, ChangePasswordRequestDTO request) {
        log.info("Changing password for user with id: {}...", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot change data of another admin");
        }

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        log.info("Validating old password for user with id: {}...", id);
        UserUtils.checkPasswords(oldPassword, newPassword, user.getPassword(), passwordEncoder);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password change successful for user with id: {}", id);
    }

    public UserDTO updateUser(Long id, ChangeUserDataRequestDTO changeUserDataRequestDTO) {
        log.info("Updating user with id: {}...", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot change data of another admin");
        }

        UserUtils.updateUserFields(user, changeUserDataRequestDTO, userRepository);
        User updatedUser = userRepository.save(user);
        log.info("User with id: {} successfully updated", id);
        return UserUtils.convertUserToUserDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}...", id);

        User user = getUserByIdAndIsDeletedCheck(id);
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot delete another admin");
        }
        user.setDeleted(true);
        userRepository.save(user);
        log.info("User with id: {} successfully deleted", id);
    }

    public UserDTO undeleteUser(Long id) {
        log.info("Restoring user with id:  {}...", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (!user.isDeleted()) {
            throw new IllegalArgumentException("User is not deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot restore another admin");
        }
        user.setDeleted(false);
        userRepository.save(user);
        log.info("User with id: {} successfully restored", id);
        return UserUtils.convertUserToUserDTO(user);
    }

    private User getUserByIdAndIsDeletedCheck(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (user.isDeleted()) {
            log.error("User with id: {} is deleted", id);
            throw new UserIsDeletedException("User is already deleted");
        }
        return user;
    }
}
