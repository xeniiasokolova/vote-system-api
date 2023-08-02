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
        System.out.println("Пароль перед шифрованием" + user.getPassword());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setUserRole(Role.USER);
        userRepository.save(user);
        System.out.println("Пароль после шифрования" + user.getPassword());
        return true;
    }

    /**
     * Создание нового пользователя
     *
     * @param user - пользователь, которого будем добавлять
     * @return сохраненного пользователя
     */
    @Transactional
    public User create(User user) {
        log.info("create {}", user);
        user.setRegistered(LocalDateTime.now());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<User> readAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "registered"));
    }

    /**
     * Получить пользователя по id
     *
     * @param id - идентификатор
     * @return возвращаем пользователя
     */
    public User read(Long id) {
        return userRepository.findById(id).get();
    }

    public User readByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * Обновить пользователя по id
     *
     * @param user - пользователь
     * @param id   - идентификатор
     */
    public void update(User user, Long id) {
        User userOld = read(id);
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
     * Удалить пользователь по id
     *
     * @param id - идентификатор
     */
    public void delete(Long id) {
        log.info("delete {}", id);
        userRepository.deleteById(id);
    }

    public String getRoleByUsername(String username) {
        return readByUsername(username).getUserRole().name();
    }

    public boolean isExistsUserByEmail(User user) {
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }

    public boolean isExistsUserByUsername(User user) {
        return userRepository.findByUsername(user.getUsername()) != null;
    }

    public boolean isPasswordsEqual(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

}
