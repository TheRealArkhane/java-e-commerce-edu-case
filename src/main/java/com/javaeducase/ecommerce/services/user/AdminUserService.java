package com.javaeducase.ecommerce.services.user;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.InsufficientAdminPrivilegesException;
import com.javaeducase.ecommerce.exceptions.UserIsDeletedException;
import com.javaeducase.ecommerce.exceptions.UserNotFoundException;
import com.javaeducase.ecommerce.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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

    public UserDTO updateUser(Long id, UserDTO userDTO) throws AccessDeniedException {
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
