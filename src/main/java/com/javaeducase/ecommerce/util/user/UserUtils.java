package com.javaeducase.ecommerce.util.user;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public class UserUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    public static void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Неправильный формат email");
        }
    }

    public static void checkEmailExists(String email, UserRepository userRepository) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
    }

    public static void checkPasswords(String oldPassword, String newPassword, String storedPassword, BCryptPasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(oldPassword, storedPassword)) {
            throw new IllegalArgumentException("Старый пароль неверен");
        }
        if (passwordEncoder.matches(newPassword, storedPassword)) {
            throw new IdenticalPasswordException("Новый пароль не может быть идентичен старому");
        }
    }

    public static UserDTO convertToDTO(User user) {
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
