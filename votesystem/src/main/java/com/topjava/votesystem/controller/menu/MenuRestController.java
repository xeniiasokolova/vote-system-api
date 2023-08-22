package com.topjava.votesystem.controller.menu;

import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.model.Vote;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController extends AbstractMenuController {
    static final String REST_URL = "/rest/restaurants/{id}/menu";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDish(@PathVariable Long id, @RequestBody Dish dish) {
        return ResponseEntity.ok(super.create(dish, id));
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<Void> updateDish(@PathVariable Long dishId, @RequestBody Dish dish) {
        boolean isUpdated = super.update(dish, dishId);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long dishId) {
        super.delete(dishId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishesByRestaurant() {
        return ResponseEntity.ok(super.getAll());
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<Dish> getDish(@PathVariable Long dishId) {
        Dish dish = super.get(dishId);
        if (dish != null) {
            return ResponseEntity.ok(dish);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{dishId}")
    public ResponseEntity<?> voteForDish(@PathVariable Long dishId, Model model,
                                         @RequestParam("voteConfirmation") String confirmationFlag) {
        User user = (User) model.getAttribute("user");
        Dish dish = super.get(dishId);
        if ("true".equals(confirmationFlag) || !super.hasUserVoteToday(user)) {
            super.voteForDish(new Vote(
                    user,
                    dish.getRestaurant(),
                    dish));
        }
        return ResponseEntity.ok().build();
    }
}
