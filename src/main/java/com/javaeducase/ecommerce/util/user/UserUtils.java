package com.javaeducase.ecommerce.util.user;

import com.javaeducase.ecommerce.dto.user.ChangeUserDataRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    public static void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Wrong email format");
        }
    }

    public static void checkEmailExists(String email, UserRepository userRepository) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email: " + email + " is already exists");
        }
    }

    public static void checkPasswords(String oldPassword, String newPassword, String storedPassword, BCryptPasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(oldPassword, storedPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, storedPassword)) {
            throw new IdenticalPasswordException("New password cannot be the same as old password");
        }
    }

    public static UserDTO convertUserToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDeleted(user.isDeleted());
        return dto;
    }

    public static void updateUserFields(
            User user,
            ChangeUserDataRequestDTO changeUserDataRequestDTO,
            UserRepository userRepository) {

        if (changeUserDataRequestDTO.getEmail() != null
                && !changeUserDataRequestDTO.getEmail().equals(user.getEmail())) {
            validateEmail(changeUserDataRequestDTO.getEmail());
            checkEmailExists(changeUserDataRequestDTO.getEmail(), userRepository);
            user.setEmail(changeUserDataRequestDTO.getEmail());
        }

        if (changeUserDataRequestDTO.getFirstName() != null
                && !changeUserDataRequestDTO.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(changeUserDataRequestDTO.getFirstName());
        }

        if (changeUserDataRequestDTO.getLastName() != null
                && !changeUserDataRequestDTO.getLastName().equals(user.getLastName())) {
            user.setLastName(changeUserDataRequestDTO.getLastName());
        }
    }
}
