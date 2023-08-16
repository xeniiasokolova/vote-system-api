package com.topjava.votesystem.controller.restaurant;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.service.MenuService;
import com.topjava.votesystem.service.RestaurantService;
import com.topjava.votesystem.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractRestaurantController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService service;
    @Autowired
    private VoteService voteService;
    @Autowired
    private MenuService menuService;


    //get restaurant by id
    public Restaurant get(long id) {
        log.info("get restaurant {}", id);
        return service.get(id);
    }

    //delete restaurant and all it's dishes and votes
    public void delete(long id) {
        log.info("delete restaurant {}", id);
        Restaurant restaurant = service.get(id);
        //delete votes for restaurant
        voteService.deleteAllVotesByRestaurantId(id);
        //delete menu
        menuService.deleteByRestaurant(restaurant);
        //delete restaurant
        service.delete(id);
    }

    //get all restaurants
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return service.getAll();
    }

    //add new restaurant
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        return service.create(restaurant);
    }

    //update restaurant by id
    public void update(Restaurant restaurant, long id) {
        log.info("update restaurant {} with id = {}", restaurant, id);
        service.update(restaurant, id);
    }

}
