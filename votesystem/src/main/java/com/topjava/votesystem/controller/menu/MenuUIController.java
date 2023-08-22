package com.topjava.votesystem.controller.menu;

import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.model.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = MenuUIController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuUIController extends AbstractMenuController {
    static final String REST_URL = "/restaurants/{id}/menu";

    @GetMapping
    public String showMenuPage(@PathVariable Long id, Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("dishes", super.getDishesByRestaurantId(id));
        model.addAttribute("restaurant", super.getRestaurant(id));
        model.addAttribute("voteError", super.canUserChangeVote(user));
        model.addAttribute("voteConfirmation", super.hasUserVoteToday(user));
        return "menu/menu";
    }

    @GetMapping("/add")
    public String showAddDishForm(@PathVariable Long id, Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("restaurant", super.getRestaurant(id));
        return "menu/add-dish";
    }

    @GetMapping("/update/{dishId}")
    public String showUpdateDishForm(@PathVariable Long dishId, Model model) {
        model.addAttribute("dish", super.get(dishId));
        return "menu/update-dish";
    }
}
