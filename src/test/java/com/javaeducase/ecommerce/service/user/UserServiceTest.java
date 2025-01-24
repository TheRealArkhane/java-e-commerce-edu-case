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
        String email = "test@test.dev";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getUserWithEmailAndIsDeletedCheck(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertFalse(result.isDeleted());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void getUserWithEmailAndIsDeletedCheck_UserNotFound() {
        String email = "nonexist@test.dev";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserWithEmailAndIsDeletedCheck(email));
        assertEquals("User with email: " + email + " is not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("nonexist@test.dev");
    }

    @Test
    void getUserWithEmailAndIsDeletedCheck_UserIsDeleted() {
        String email = "test@test.dev";
        user.setDeleted(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserIsDeletedException exception = assertThrows(
                UserIsDeletedException.class, () -> userService.getUserWithEmailAndIsDeletedCheck(email));
        assertEquals("User with email: " + email + " is deleted", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }


    @Test
    void getCurrentUser_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))));
        when(userRepository.findByEmail("test@test.dev")).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void changePassword_Success() {
        String oldPassword = "password";
        String newPassword = "newPassword";

        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );

        userService.changePassword(new ChangePasswordRequestDTO(oldPassword, newPassword));

        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_WrongOldPassword() {
        String oldPassword = "WrongOldPassword";
        String newPassword = "newPassword";

        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(new ChangePasswordRequestDTO(oldPassword, newPassword)));
        assertEquals("Old password is incorrect", ex.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void changePassword_IdenticalOldAndNewPasswords() {
        String oldPassword = "password";
        String newPassword = "password";

        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );

        IdenticalPasswordException ex = assertThrows(IdenticalPasswordException.class,
                () -> userService.changePassword(new ChangePasswordRequestDTO(oldPassword, newPassword)));
        assertEquals("New password cannot be the same as old password", ex.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void updateCurrentUser_Success() {
        ChangeUserDataRequestDTO changeUserDataRequestDTO = new ChangeUserDataRequestDTO();
        changeUserDataRequestDTO.setFirstName("newFirstName");
        changeUserDataRequestDTO.setLastName("newLastName");
        changeUserDataRequestDTO.setEmail("newEmail@test.dev");

        User updatedUser = new User();
        updatedUser.setEmail(changeUserDataRequestDTO.getEmail());
        updatedUser.setFirstName(changeUserDataRequestDTO.getFirstName());
        updatedUser.setLastName(changeUserDataRequestDTO.getLastName());
        updatedUser.setDeleted(false);
        updatedUser.setRole(Role.USER);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))));
        when(userRepository.findByEmail("test@test.dev")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDTO result = userService.updateCurrentUser(changeUserDataRequestDTO);

        assertNotNull(result);
        assertEquals(result.getFirstName(), changeUserDataRequestDTO.getFirstName());
        assertEquals(result.getLastName(), changeUserDataRequestDTO.getLastName());
        assertEquals(result.getEmail(), changeUserDataRequestDTO.getEmail());

        verify(userRepository, times(1)).findByEmail("test@test.dev");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateCurrentUser_WrongEmailFormat() {
        ChangeUserDataRequestDTO changeUserDataRequestDTO = new ChangeUserDataRequestDTO();
        changeUserDataRequestDTO.setEmail("wrongFormatEmail");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))));
        when(userRepository.findByEmail("test@test.dev")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.updateCurrentUser(changeUserDataRequestDTO));

        assertEquals("Wrong email format", ex.getMessage());
        verify(userRepository, times(1)).findByEmail("test@test.dev");
    }

    @Test
    void updateCurrentUser_EmailAlreadyExists() {
        String existEmail = "exist@test.dev";
        User existingUser = new User();
        existingUser.setEmail(existEmail);

        ChangeUserDataRequestDTO changeUserDataRequestDTO = new ChangeUserDataRequestDTO();
        changeUserDataRequestDTO.setEmail(existEmail);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))));
        when(userRepository.findByEmail("test@test.dev")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(existEmail)).thenReturn(Optional.of(existingUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.updateCurrentUser(changeUserDataRequestDTO));

        assertEquals("User with email: "
                + changeUserDataRequestDTO.getEmail()
                + " is already exists",
                ex.getMessage());
        verify(userRepository, times(1)).findByEmail("test@test.dev");
        verify(userRepository, times(1)).findByEmail(existEmail);
    }

    @Test
    void deleteCurrentUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );

        userService.deleteCurrentUser();

        assertTrue(user.isDeleted());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }
}
