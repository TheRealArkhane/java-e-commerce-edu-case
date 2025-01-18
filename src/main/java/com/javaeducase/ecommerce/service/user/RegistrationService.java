package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.RegistrationDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.Role;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import com.javaeducase.ecommerce.util.user.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDTO registerUser(RegistrationDTO registrationDTO) {
        log.info("Starting registration for user with email: {}...", registrationDTO.getEmail());
        UserUtils.validateEmail(registrationDTO.getEmail());
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User user = new User();
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(Role.USER);
        user.setDeleted(false);

        User savedUser = userRepository.save(user);
        log.info("User with email: {} successfully registered", registrationDTO.getEmail());
        return UserUtils.convertToDTO(savedUser);
    }
}
