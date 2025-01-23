package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.ChangeUserDataRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exception.user.UserNotFoundException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import com.javaeducase.ecommerce.util.user.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user with email: {}...", email);
        User user = getUserWithEmailAndIsDeletedCheck(email);
        log.info("User with email: {} successfully logged in", email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public User getCurrentUser() {
        log.info("Fetching current user...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            log.error("Current user not found in security context");
            throw new UserNotFoundException("Current user not found");
        }

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("Searching for user with email: {}...", email);
        return getUserWithEmailAndIsDeletedCheck(email);
    }

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.info("Changing password for current user...");

        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();
        User currentUser = getCurrentUser();

        log.info("Checking old password for user with email: {}...", currentUser.getEmail());
        UserUtils.checkPasswords(oldPassword, newPassword, currentUser.getPassword(), passwordEncoder);

        log.info("Setting new password for user with email: {}...", currentUser.getEmail());
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
        log.info("Password for user with email: {} successfully changed", currentUser.getEmail());
    }

    public UserDTO updateCurrentUser(ChangeUserDataRequestDTO changeUserDataRequestDTO) {
        log.info("Updating data of current user...");
        User currentUser = getCurrentUser();
        UserUtils.updateUserFields(currentUser, changeUserDataRequestDTO, userRepository);
        User updatedUser = userRepository.save(currentUser);
        log.info("User with id: {} successfully updated", currentUser.getId());
        return UserUtils.convertUserToUserDTO(updatedUser);
    }

    public void deleteCurrentUser() {
        User currentUser = getCurrentUser();
        log.info("Deleting user with id: {}...", currentUser.getId());
        currentUser.setDeleted(true);
        userRepository.save(currentUser);
        log.info("User with id: {} successfully deleted", currentUser.getId());
    }

    private User getUserWithEmailAndIsDeletedCheck(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  new UserNotFoundException("User with email: " + email + " not found"));
        if (user.isDeleted()) {
            log.error("User with email: {} is deleted", email);
            throw new UserIsDeletedException("User is already deleted");
        }
        return user;
    }
}
