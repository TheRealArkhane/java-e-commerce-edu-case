package com.javaeducase.ecommerce.service.user;

import com.javaeducase.ecommerce.dto.user.ChangePasswordRequestDTO;
import com.javaeducase.ecommerce.dto.user.ChangeUserDataRequestDTO;
import com.javaeducase.ecommerce.dto.user.UserDTO;
import com.javaeducase.ecommerce.entity.user.Role;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.exception.user.InsufficientAdminPrivilegesException;
import com.javaeducase.ecommerce.exception.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exception.user.UserNotFoundException;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserService adminUserService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(2L);
        user1.setFirstName("Test");
        user1.setLastName("User");
        user1.setEmail("testuser@test.dev");
        user1.setPassword("password1");
        user1.setRole(Role.USER);
        user1.setDeleted(false);

        user2 = new User();
        user2.setId(3L);
        user2.setFirstName("Test");
        user2.setLastName("Admin");
        user2.setEmail("testadmin@test.dev");
        user2.setPassword("password2");
        user2.setRole(Role.ADMIN);
        user2.setDeleted(false);
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = adminUserService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        UserDTO user1DTO = result.get(0);
        assertEquals(user1.getId(), user1DTO.getId());
        assertEquals(user1.getEmail(), user1DTO.getEmail());
        assertEquals(user1.getFirstName(), user1DTO.getFirstName());
        assertEquals(user1.getLastName(), user1DTO.getLastName());
        assertEquals(user1.getRole(), user1DTO.getRole());
        assertEquals(user1.isDeleted(), user1DTO.isDeleted());

        UserDTO user2DTO = result.get(1);
        assertEquals(user2.getId(), user2DTO.getId());
        assertEquals(user2.getEmail(), user2DTO.getEmail());
        assertEquals(user2.getFirstName(), user2DTO.getFirstName());
        assertEquals(user2.getLastName(), user2DTO.getLastName());
        assertEquals(user2.getRole(), user2DTO.getRole());
        assertEquals(user2.isDeleted(), user2DTO.isDeleted());

        verify(userRepository).findAll();
    }

    @Test
    void getUserById_Success() {
        setUpUserInRepository(user1.getId(), user1);

        UserDTO result = adminUserService.getUserById(user1.getId());

        assertEquals(user1.getId(), result.getId());
        assertEquals(user1.getEmail(), result.getEmail());
        assertEquals(user1.getFirstName(), result.getFirstName());
        assertEquals(user1.getLastName(), result.getLastName());
        assertEquals(user1.getRole(), result.getRole());
        assertEquals(user1.isDeleted(), result.isDeleted());

        verify(userRepository).findById(user1.getId());
    }

    @Test
    void getUserById_UserNotFound() {
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> adminUserService.getUserById(user1.getId()));
        assertEquals("User with id: " + user1.getId() + " not found", ex.getMessage());
    }

    @Test
    void changeUserPassword_Success() {
        String oldPassword = "password1";
        String newPassword = "newPassword";

        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO(oldPassword, newPassword);

        setUpUserInRepository(user1.getId(),user1);
        when(passwordEncoder.matches(oldPassword, user1.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        adminUserService.changeUserPassword(user1.getId(), requestDTO);

        assertEquals(user1.getPassword(), "encodedNewPassword");

        verify(userRepository).save(user1);
    }

    @Test
    void changeUserPassword_WrongOldPassword() {
        String oldPassword = "WrongPassword";
        String newPassword = "newPassword";

        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO(oldPassword, newPassword);

        setUpUserInRepository(user1.getId(),user1);
        when(passwordEncoder.matches(oldPassword, user1.getPassword())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adminUserService.changeUserPassword(user1.getId(), requestDTO));
        assertEquals("Old password is incorrect", ex.getMessage());
    }

    @Test
    void changeUserPassword_IdenticalOldAndNewPasswords() {
        String oldPassword = "password1";
        String newPassword = "password1";

        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO(oldPassword, newPassword);

        setUpUserInRepository(user1.getId(),user1);
        when(passwordEncoder.matches(oldPassword, user1.getPassword())).thenReturn(true);

        IdenticalPasswordException ex = assertThrows(
                IdenticalPasswordException.class,
                () -> adminUserService.changeUserPassword(user1.getId(), requestDTO));
        assertEquals("New password cannot be the same as old password", ex.getMessage());
    }


    @Test
    void changeUserPassword_InsufficientAdminPrivileges() {
        String oldPassword = "password2";
        String newPassword = "NewPassword";

        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO(oldPassword, newPassword);

        setUpUserInRepository(user2.getId(),user2);

        InsufficientAdminPrivilegesException ex = assertThrows(
                InsufficientAdminPrivilegesException.class,
                () -> adminUserService.changeUserPassword(user2.getId(), requestDTO));
        assertEquals("Admin cannot change data of another admin", ex.getMessage());
    }

    @Test
    void updateUser_Success() {
        String newFirstName = "newName";
        String newLastName = "newLastName";
        String newEmail = "newEmail@test.dev";
        ChangeUserDataRequestDTO requestDTO = new ChangeUserDataRequestDTO();
        requestDTO.setFirstName(newFirstName);
        requestDTO.setLastName(newLastName);
        requestDTO.setEmail(newEmail);
        setUpUserInRepository(user1.getId(), user1);
        when(userRepository.save(user1)).thenReturn(user1);

        UserDTO result = adminUserService.updateUser(user1.getId(), requestDTO);
        assertNotNull(result);
        assertEquals(user1.getFirstName(), result.getFirstName());
        assertEquals(user1.getLastName(), result.getLastName());
        assertEquals(user1.getEmail(), result.getEmail());

        verify(userRepository).save(user1);
    }

    @Test
    void updateUser_WrongEmailFormat() {
        String newEmail = "newEmailWrongFormat";
        ChangeUserDataRequestDTO requestDTO = new ChangeUserDataRequestDTO();
        requestDTO.setEmail(newEmail);
        setUpUserInRepository(user1.getId(), user1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adminUserService.updateUser(user1.getId(), requestDTO));
        assertEquals("Wrong email format", ex.getMessage());
    }

    @Test
    void updateUser_EmailAlreadyExists() {
        String newEmail = user2.getEmail();
        ChangeUserDataRequestDTO requestDTO = new ChangeUserDataRequestDTO();
        requestDTO.setEmail(newEmail);
        setUpUserInRepository(user1.getId(), user1);
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.of(user2));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adminUserService.updateUser(user1.getId(), requestDTO));
        assertEquals("User with email: " + newEmail + " is already exists", ex.getMessage());
    }

    @Test
    void updateUser_InsufficientAdminPrivileges() {
        ChangeUserDataRequestDTO requestDTO = new ChangeUserDataRequestDTO();
        setUpUserInRepository(user2.getId(), user2);

        InsufficientAdminPrivilegesException ex = assertThrows(
                InsufficientAdminPrivilegesException.class,
                () -> adminUserService.updateUser(user2.getId(), requestDTO));
        assertEquals("Admin cannot change data of another admin", ex.getMessage());
    }

    @Test
    void deleteUser_Success() {
        setUpUserInRepository(user1.getId(), user1);
        when(userRepository.save(user1)).thenReturn(user1);

        adminUserService.deleteUser(user1.getId());

        assertTrue(user1.isDeleted());

        verify(userRepository).findById(user1.getId());
        verify(userRepository).save(user1);
    }

    @Test
    void deleteUser_UserAlreadyDeleted() {
        user1.setDeleted(true);
        setUpUserInRepository(user1.getId(), user1);

        UserIsDeletedException ex = assertThrows(
                UserIsDeletedException.class,
                () -> adminUserService.deleteUser(user1.getId()));
        assertEquals("User is already deleted", ex.getMessage());
    }

    @Test
    void deleteUser_InsufficientAdminPrivileges() {
        setUpUserInRepository(user2.getId(), user2);

        InsufficientAdminPrivilegesException ex = assertThrows(
                InsufficientAdminPrivilegesException.class,
                () -> adminUserService.deleteUser(user2.getId()));
        assertEquals("Admin cannot delete another admin", ex.getMessage());
    }

    @Test
    void undeleteUser_Success() {
        user1.setDeleted(true);
        setUpUserInRepository(user1.getId(), user1);
        when(userRepository.save(user1)).thenReturn(user1);

        adminUserService.undeleteUser(user1.getId());

        assertFalse(user1.isDeleted());

        verify(userRepository).findById(user1.getId());
        verify(userRepository).save(user1);
    }

    @Test
    void undeleteUser_UserIsNotDeleted() {
        setUpUserInRepository(user1.getId(), user1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adminUserService.undeleteUser(user1.getId()));
        assertEquals("User is not deleted", ex.getMessage());
    }

    @Test
    void undeleteUser_InsufficientAdminPrivileges() {
        user2.setDeleted(true);
        setUpUserInRepository(user2.getId(), user2);

        InsufficientAdminPrivilegesException ex = assertThrows(
                InsufficientAdminPrivilegesException.class,
                () -> adminUserService.undeleteUser(user2.getId()));
        assertEquals("Admin cannot restore another admin", ex.getMessage());
    }

    private void setUpUserInRepository(Long id, User user) {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
    }
}
