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



    //get user by id
    public User get(long id) {
        log.info("get user {}", id);
        return service.get(id);
    }

    //delete user and all it's votes
    public void delete(long id) {
        log.info("delete user {}", id);
        //delete votes for user
        voteService.deleteAllVotesByUserId(id);
        //delete user
        service.delete(id);
    }

    //get all restaurants
    public List<User> getAll() {
        log.info("get all users");
        return service.getAll();
    }

    //add new restaurant
    public User create(User user) {
        log.info("create user {}", user);
        return service.create(user);
    }

    //update restaurant by id
    public void update(User user, long id) {
        log.info("update user {} with id = {}", user, id);
        service.update(user, id);
    }

    public boolean isExistsUserByUsername(User user) {
        return service.isExistsUserByUsername(user);
    }

    public boolean isExistsUserByEmail(User user) {
        return service.isExistsUserByEmail(user);
    }

    public boolean isPasswordsEqual(String rawPassword, String encodedPassword) {
        return service.isPasswordsEqual(rawPassword, encodedPassword);
    }
}
