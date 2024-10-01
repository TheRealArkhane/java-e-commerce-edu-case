package com.javaeducase.ecommerce.services.user;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.user.InsufficientAdminPrivilegesException;
import com.javaeducase.ecommerce.exceptions.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exceptions.user.UserNotFoundException;
import com.javaeducase.ecommerce.repositories.user.UserRepository;
import com.javaeducase.ecommerce.utils.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserUtils userUtils; // Add this

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userUtils::convertToDTO)
                .collect(Collectors.toList());
    }

    public void changePassword(Long id, String oldPassword, String newPassword, PasswordEncoder passwordEncoder) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Администратор не может изменять данные другого администратора");
        }
        userUtils.checkPasswords(oldPassword, newPassword, user.getPassword(), passwordEncoder);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Администратор не может изменять данные другого администратора");
        }
        userUtils.validateEmail(userDTO.getEmail());
        userUtils.checkEmailExists(userDTO.getEmail());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user = userRepository.save(user);
        return userUtils.convertToDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        if (user.getRole().name().equals("ADMIN")) {
            throw new InsufficientAdminPrivilegesException("Администратор не может удалять данные другого администратора");
        }
        user.setDeleted(true);
        userRepository.save(user);
    }
}
