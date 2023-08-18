package com.topjava.votesystem;

import com.topjava.votesystem.model.Role;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.repository.UserRepository;
import com.topjava.votesystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        User loadedUser = (User) userService.loadUserByUsername(username);

        assertNotNull(loadedUser);
        assertEquals(username, loadedUser.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void testSignUpUser_UserExistsByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertFalse(userService.signUpUser(user));
    }

    @Test
    public void testSignUpUser_UserDoesNotExistByEmail() {
        User user = new User();
        user.setEmail("newuser@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertTrue(userService.signUpUser(user));
        assertEquals(Role.USER, user.getUserRole());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("newUser");
        String rawPassword = "password";
        String encodedPassword = bCryptPasswordEncoder.encode("password");

        when(bCryptPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(encodedPassword, createdUser.getPassword());
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User retrievedUser = userService.get(userId);

        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getId());
    }

    @Test
    public void testReadUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        User retrievedUser = userService.readByUsername(username);

        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        userService.update(updatedUser, userId);

        assertEquals(updatedUser.getUsername(), existingUser.getUsername());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testGetRoleByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setUserRole(Role.ADMIN);

        when(userRepository.findByUsername(username)).thenReturn(user);

        String role = userService.getRoleByUsername(username);

        assertEquals(Role.ADMIN.name(), role);
    }

    @Test
    public void testIsExistsUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertTrue(userService.isExistsUserByEmail(user));
    }

    @Test
    public void testIsExistsUserByUsername() {
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        assertTrue(userService.isExistsUserByUsername(user));
    }
}
