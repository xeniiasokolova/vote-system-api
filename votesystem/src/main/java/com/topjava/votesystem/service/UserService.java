package com.topjava.votesystem.service;

import com.topjava.votesystem.model.Role;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String USER_NOT_FOUND_MSG = "user with username %s not found";

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }
        return user;
    }

    public boolean signUpUser(User user) {
        if (isExistsUserByEmail(user)) {
            return false; // User already exists
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setUserRole(Role.USER);
        userRepository.save(user);
        return true;
    }

    /**
     * Creating a new user
     *
     * @param user - user to be added
     * @return saved user
     */
    @Transactional
    public User create(User user) {
        log.info("create {}", user);
        user.setRegistered(LocalDateTime.now());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Get all users
     *
     * @return list of all users sorted by registration date
     */
    public List<User> getAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "registered"));
    }

    /**
     * Get user by id
     *
     * @param id - id
     * @return return user
     */
    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * Get user by username
     *
     * @param username - username
     * @return return user
     */
    public User readByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * Update user by id
     *
     * @param user - user
     * @param id   - identifier
     */
    public void update(User user, Long id) {
        User userOld = get(id);
        userOld.setFirstName(user.getFirstName());
        userOld.setLastName(user.getLastName());
        userOld.setEmail(user.getEmail());
        userOld.setUsername(user.getUsername());

        if (user.getUserRole() != null)
            userOld.setUserRole(user.getUserRole());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (!user.getPassword().equals(userOld.getPassword())) {
                String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                userOld.setPassword(encodedPassword);
            }
        }

        log.info("update {} with id={}", user, id);
        userRepository.save(userOld);
    }

    /**
     * Delete user by id
     *
     * @param id - identifier
     */
    public void delete(Long id) {
        log.info("delete {}", id);
        userRepository.deleteById(id);
    }

    /**
     * Get role by username
     *
     * @param username - username user
     * @return role
     */
    public String getRoleByUsername(String username) {
        return readByUsername(username).getUserRole().name();
    }

    /**
     * Check if there is a user with this email
     *
     * @param user - user
     * @return true if user existed
     */
    public boolean isExistsUserByEmail(User user) {
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }


    /**
     * Check if there is a user with this username
     *
     * @param user - user
     * @return true if user existed
     */
    public boolean isExistsUserByUsername(User user) {
        return userRepository.findByUsername(user.getUsername()) != null;
    }

    /**
     * Check if passwords match
     *
     * @param rawPassword     - password without encoding
     * @param encodedPassword - password with encoding
     * @return true if passwords match
     */
    public boolean isPasswordsEqual(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

}
