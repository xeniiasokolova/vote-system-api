package com.topjava.votesystem.controller.menu;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.model.Vote;
import com.topjava.votesystem.service.MenuService;
import com.topjava.votesystem.service.RestaurantService;
import com.topjava.votesystem.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractMenuController extends BaseController {

    @Autowired
    private MenuService service;
    @Autowired
    private VoteService voteService;
    @Autowired
    private RestaurantService restaurantService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Get dish by id
     *
     * @param id - dish's identity
     * @return dish
     */
    public Dish get(long id) {
        log.info("get dish {}", id);
        return service.get(id);
    }

    /**
     * Delete dish and all it's votes
     *
     * @param id - dish's identity
     */
    public void delete(long id) {
        log.info("delete dish {}", id);
        //delete votes for restaurant's dish
        voteService.deleteAllVotesByDishId(id);
        //delete dish
        service.delete(id);
    }

    /**
     * Get all dishes
     *
     * @return list of dishes
     */
    public List<Dish> getAll() {
        log.info("get all dishes");
        return service.getAll();
    }

    /**
     * Add new dish
     *
     * @param dish - dish from form
     * @param id   - dish's identity
     * @return created dish
     */
    public Dish create(Dish dish, long id) {
        log.info("create dish {}", dish);
        dish.setRestaurant(getRestaurant(id));
        return service.create(dish);
    }

    /**
     * Update dish by id
     *
     * @param dish - dish to be updated
     * @param id   - dish's identity
     * @return true if dish updated
     */
    public boolean update(Dish dish, long id) {
        log.info("update dish {} with id = {}", dish, id);
        return service.update(dish, id);
    }

    /**
     * Get dish's restaurant
     *
     * @param id - restaurant's identity
     * @return restaurant
     */
    public Restaurant getRestaurant(Long id) {
        return restaurantService.get(id);
    }

    /**
     * Get all restaurant's dishes
     *
     * @param id - restaurant identity
     * @return list of dishes
     */
    public List<Dish> getDishesByRestaurantId(Long id) {
        log.info("get all dishes by restaurant id = {}", id);
        return service.getDishesByRestaurantId(id);
    }

    /**
     * User can change vote until 11 am
     *
     * @param user - user to be updated vote
     * @return true if user can change vote
     */
    public boolean canUserChangeVote(User user) {
        return voteService.canUserChangeVote(user);
    }

    /**
     * Check user is voted today or not
     *
     * @param user - user to check vote
     * @return true if user voted today
     */
    public boolean hasUserVoteToday(User user) {
        return voteService.hasVotedToday(user);
    }

    /**
     * Add vote to vote's list
     *
     * @param vote - vote to be added
     */
    public void voteForDish(Vote vote) {
        voteService.vote(vote);
    }
}
