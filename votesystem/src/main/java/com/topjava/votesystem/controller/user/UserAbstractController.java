package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import com.topjava.votesystem.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public abstract class UserAbstractController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;
    @Autowired
    private VoteService voteService;


    /**
     * Get user by identity
     *
     * @param id - user's identity
     * @return user
     */
    public User get(long id) {
        log.info("get user {}", id);
        return service.get(id);
    }

    /**
     * Delete user and all it's votes
     *
     * @param id - user's identity
     */
    public void delete(long id) {
        log.info("delete user {}", id);
        //delete votes for user
        voteService.deleteAllVotesByUserId(id);
        //delete user
        service.delete(id);
    }

    /**
     * Get all users
     *
     * @return list of all users
     */
    public List<User> getAll() {
        log.info("get all users");
        return service.getAll();
    }

    /**
     * Add new user
     *
     * @param user - user to be added
     * @return - added user
     */
    public User create(User user) {
        log.info("create user {}", user);
        return service.create(user);
    }

    /**
     * Update user by id
     *
     * @param user - user to be updated
     * @param id   - user's identity
     */
    public void update(User user, long id) {
        log.info("update user {} with id = {}", user, id);
        service.update(user, id);
    }

    /**
     * Check if there is a user with this username
     *
     * @param user - user to be checked
     * @return true if user is found
     */
    public boolean isExistsUserByUsername(User user) {
        return service.isExistsUserByUsername(user);
    }

    /**
     * Check if there is a user with this email
     *
     * @param user - user to be checked
     * @return true if user is found
     */
    public boolean isExistsUserByEmail(User user) {
        return service.isExistsUserByEmail(user);
    }

    /**
     * Check if passwords match
     *
     * @param rawPassword     - password without encoding
     * @param encodedPassword - password with encoding
     * @return true if passwords match
     */
    public boolean isPasswordsEqual(String rawPassword, String encodedPassword) {
        return service.isPasswordsEqual(rawPassword, encodedPassword);
    }
}
