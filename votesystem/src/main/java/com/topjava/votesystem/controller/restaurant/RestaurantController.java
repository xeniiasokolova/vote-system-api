package com.topjava.votesystem.controller.restaurant;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.service.MenuService;
import com.topjava.votesystem.service.RestaurantService;
import com.topjava.votesystem.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController extends BaseController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final VoteService voteService;

    //get all restaurants
    @GetMapping
    ResponseEntity<List<Restaurant>> readAll() {
        final List<Restaurant> restaurants = restaurantService.readAll();
        return restaurants != null && !restaurants.isEmpty() ?
                new ResponseEntity<>(restaurants, HttpStatus.OK) :
                new ResponseEntity<>(restaurants, HttpStatus.NOT_FOUND);
    }

    //add new restaurant
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Restaurant restaurant) {
        restaurantService.create(restaurant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //get restaurant by id
    @GetMapping(value = "/{id}")
    public ResponseEntity<Restaurant> read(@PathVariable(name = "id") Long id) {
        final Restaurant restaurant = restaurantService.read(id);
        return restaurant != null ?
                new ResponseEntity<>(restaurant, HttpStatus.OK) :
                new ResponseEntity<>(restaurant, HttpStatus.NOT_FOUND);
    }

    //update restaurant by id
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody Restaurant restaurant) {
        final boolean isUpdate = restaurantService.update(restaurant, id);
        return isUpdate ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    //delete restaurant and all it's dishes and votes
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        try {
            Restaurant restaurant = restaurantService.read(id);
            //очищаем список голоса
            voteService.deleteAllVotesByRestaurantId(id);
            //очищаем блюда
            menuService.deleteByRestaurant(restaurant);
            //удаляем рестораны
            restaurantService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}

