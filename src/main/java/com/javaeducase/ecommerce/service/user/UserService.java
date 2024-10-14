package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exception.user.UserNotFoundException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import com.javaeducase.ecommerce.util.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();
        User currentUser = getCurrentUser();
        if (currentUser.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        UserUtils.checkPasswords(oldPassword, newPassword, currentUser.getPassword(), passwordEncoder);
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        return UserUtils.convertToDTO(user);
    }

    public UserDTO updateCurrentUser(UserDTO userDTO) {
        User currentUser = getCurrentUser();
        if (currentUser.isDeleted()) {
            throw new UserIsDeletedException("Пользователь ранее был удален");
        }
        if (!currentUser.getEmail().equals(userDTO.getEmail())) {
            UserUtils.validateEmail(userDTO.getEmail());  // Проверяем формат нового email
            UserUtils.checkEmailExists(userDTO.getEmail(), userRepository);  // Проверяем наличие email в БД
            currentUser.setEmail(userDTO.getEmail());  // Устанавливаем новый email
        }
        currentUser.setFirstName(userDTO.getFirstName());
        currentUser.setLastName(userDTO.getLastName());
        User updatedUser = userRepository.save(currentUser);
        return UserUtils.convertToDTO(updatedUser);
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
