package com.javaeducase.ecommerce.services.user;

import com.javaeducase.ecommerce.dto.user.RegistrationDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entities.user.Role;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.repositories.user.UserRepository;
import com.javaeducase.ecommerce.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserUtils userUtils; // Add this

    public UserDTO registerUser(RegistrationDTO registrationDTO, PasswordEncoder passwordEncoder) {
        userUtils.validateEmail(registrationDTO.getEmail());
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(Role.USER);
        user.setDeleted(false);
        User savedUser = userRepository.save(user);
        return userUtils.convertToDTO(savedUser);
    }
}