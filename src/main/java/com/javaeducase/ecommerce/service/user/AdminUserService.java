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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserUtils::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return UserUtils.convertToDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found")));
    }

    public void changeUserPassword(Long id, ChangePasswordRequestDTO request) {
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("User is deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot change data of another admin");
        }
        UserUtils.checkPasswords(oldPassword, newPassword, user.getPassword(), passwordEncoder);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("User is deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot change data of another admin");
        }
        UserUtils.validateEmail(userDTO.getEmail());
        UserUtils.checkEmailExists(userDTO.getEmail(), userRepository);
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user = userRepository.save(user);
        return UserUtils.convertToDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("User is deleted");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Admin cannot delete another admin");
        }
        user.setDeleted(true);
        userRepository.save(user);
    }
}
