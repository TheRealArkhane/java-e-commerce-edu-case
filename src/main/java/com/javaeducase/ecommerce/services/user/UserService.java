package com.javaeducase.ecommerce.services.user;

import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exceptions.user.UserNotFoundException;
import com.javaeducase.ecommerce.repositories.user.UserRepository;
import com.javaeducase.ecommerce.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email: " + email + " Не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new UserNotFoundException("Current user not found");
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        return user;
    }

    public void changePassword(String oldPassword, String newPassword, PasswordEncoder passwordEncoder) {
        User currentUser = getCurrentUser();
        if (currentUser.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        userUtils.checkPasswords(oldPassword, newPassword, currentUser.getPassword(), passwordEncoder);
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        return userUtils.convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        return userUtils.convertToDTO(user);
    }

    public UserDTO updateCurrentUser(UserDTO userDTO) {
        User currentUser = getCurrentUser();
        if (currentUser.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        if (!currentUser.getEmail().equals(userDTO.getEmail())) {
            userUtils.validateEmail(userDTO.getEmail());  // Проверяем формат нового email
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует");
            }
            currentUser.setEmail(userDTO.getEmail());  // Устанавливаем новый email
        }
        currentUser.setFirstName(userDTO.getFirstName());
        currentUser.setLastName(userDTO.getLastName());
        User updatedUser = userRepository.save(currentUser);
        return userUtils.convertToDTO(updatedUser);
    }

    public void deleteCurrentUser() {
        User currentUser = getCurrentUser();
        if (currentUser.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        currentUser.setDeleted(true);
        userRepository.save(currentUser);
    }
}
