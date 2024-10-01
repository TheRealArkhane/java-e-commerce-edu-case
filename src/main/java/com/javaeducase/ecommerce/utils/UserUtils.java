package com.javaeducase.ecommerce.utils;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.repositories.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    public void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Неправильный формат email");
        }
    }

    public void checkEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
    }

    public void checkPasswords(String oldPassword, String newPassword, String storedPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(oldPassword, storedPassword)) {
            throw new IllegalArgumentException("Старый пароль неверен");
        }
        if (passwordEncoder.matches(newPassword, storedPassword)) {
            throw new IdenticalPasswordException("Новый пароль не может быть идентичен старому");
        }
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDeleted(user.isDeleted());
        return dto;
    }
}
