package com.javaeducase.ecommerce.services;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entities.user.Role;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.UserNotFoundException;
import com.javaeducase.ecommerce.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email: " + email + " Не найден"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public UserDTO registerUser(UserDTO userDTO, String password) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setDeleted(false);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new UserNotFoundException("Current user not found");
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
    }

    public void changePassword(String oldPassword, String newPassword) {
        User currentUser = getCurrentUser();
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException("Старый пароль неверен");
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return convertToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateCurrentUser(UserDTO userDTO) {
        User currentUser = getCurrentUser();
        currentUser.setFirstName(userDTO.getFirstName());
        currentUser.setLastName(userDTO.getLastName());
        User updatedUser = userRepository.save(currentUser);
        return convertToDTO(updatedUser);
    }

    public void deleteCurrentUser() {
        User currentUser = getCurrentUser();
        currentUser.setDeleted(true);
        userRepository.save(currentUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
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
