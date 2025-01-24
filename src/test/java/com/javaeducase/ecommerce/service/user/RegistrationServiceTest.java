package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.RegistrationDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.Role;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    private RegistrationDTO registrationDTO;
    private User user;

    @BeforeEach
    void setUp() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setFirstName("Test");
        registrationDTO.setLastName("User");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("password");

        user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setDeleted(false);
    }

    @Test
    void registerUser_success() {
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = registrationService.registerUser(registrationDTO);

        assertNotNull(result);
        assertEquals(registrationDTO.getEmail(), result.getEmail());
        assertEquals(registrationDTO.getFirstName(), result.getFirstName());
        assertEquals(registrationDTO.getLastName(), result.getLastName());

        verify(userRepository).findByEmail(registrationDTO.getEmail());
        verify(passwordEncoder).encode(registrationDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_WrongEmailFormat() {
        registrationDTO.setEmail("invalidEmail");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.registerUser(registrationDTO));

        assertEquals("Wrong email format", ex.getMessage());

        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.registerUser(registrationDTO));

        assertEquals("User with email: " + registrationDTO.getEmail() + " is already exists", ex.getMessage());

        verify(userRepository).findByEmail(registrationDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
}
