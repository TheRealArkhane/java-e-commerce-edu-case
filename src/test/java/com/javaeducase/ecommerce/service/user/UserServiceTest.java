package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.ChangeUserDataRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.Role;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.exception.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exception.user.UserNotFoundException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.dev");
        user.setFirstName("Test");
        user.setLastName("Tested");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setDeleted(false);
    }

    @Test
    void getUserWithEmailAndIsDeletedCheck_Success() {
        setUpUserInRepository(user.getEmail());

        User result = userService.getUserWithEmailAndIsDeletedCheck(user.getEmail());

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertFalse(result.isDeleted());
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getUserWithEmailAndIsDeletedCheck_UserNotFound() {
        String email = "nonexist@test.dev";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserWithEmailAndIsDeletedCheck(email)
        );

        assertEquals("User with email: " + email + " is not found", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserWithEmailAndIsDeletedCheck_UserIsDeleted() {
        user.setDeleted(true);
        setUpUserInRepository(user.getEmail());

        UserIsDeletedException exception = assertThrows(
                UserIsDeletedException.class,
                () -> userService.getUserWithEmailAndIsDeletedCheck(user.getEmail())
        );

        assertEquals("User with email: " + user.getEmail() + " is deleted", exception.getMessage());
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getCurrentUser_Success() {
        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());

        User result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void changePassword_Success() {
        String oldPassword = "password";
        String newPassword = "newPassword";
        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());

        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.changePassword(new ChangePasswordRequestDTO(oldPassword, newPassword));

        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_WrongOldPassword() {
        String oldPassword = "wrongPassword";
        String newPassword = "newPassword";
        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());

        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword(new ChangePasswordRequestDTO(oldPassword, newPassword))
        );

        assertEquals("Old password is incorrect", ex.getMessage());
    }

    @Test
    void changePassword_IdenticalOldAndNewPasswords() {
        String oldPassword = "password";
        String newPassword = "password";
        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());

        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);

        IdenticalPasswordException ex = assertThrows(
                IdenticalPasswordException.class,
                () -> userService.changePassword(new ChangePasswordRequestDTO(oldPassword, newPassword))
        );

        assertEquals("New password cannot be the same as old password", ex.getMessage());
    }

    @Test
    void updateCurrentUser_Success() {
        ChangeUserDataRequestDTO changeRequest = new ChangeUserDataRequestDTO();
        changeRequest.setFirstName("newFirstName");
        changeRequest.setLastName("newLastName");
        changeRequest.setEmail("newEmail@test.dev");

        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateCurrentUser(changeRequest);

        assertNotNull(result);
        assertEquals(changeRequest.getFirstName(), result.getFirstName());
        assertEquals(changeRequest.getLastName(), result.getLastName());
        assertEquals(changeRequest.getEmail(), result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void updateCurrentUser_EmailAlreadyExists() {
        String existEmail = "exist@test.dev";
        ChangeUserDataRequestDTO changeRequest = new ChangeUserDataRequestDTO();
        changeRequest.setEmail(existEmail);

        User existingUser = new User();
        existingUser.setEmail(existEmail);

        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());
        when(userRepository.findByEmail(existEmail)).thenReturn(Optional.of(existingUser));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateCurrentUser(changeRequest)
        );

        assertEquals("User with email: " + existEmail + " is already exists", ex.getMessage());
    }

    @Test
    void deleteCurrentUser_Success() {
        mockAuthentication(user.getEmail());
        setUpUserInRepository(user.getEmail());

        userService.deleteCurrentUser();

        assertTrue(user.isDeleted());
        verify(userRepository).save(user);
    }

    private void mockAuthentication(String email) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        email,
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );
    }

    private void setUpUserInRepository(String email) {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    }
}
