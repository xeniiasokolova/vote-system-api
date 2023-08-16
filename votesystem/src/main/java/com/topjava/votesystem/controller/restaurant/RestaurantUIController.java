package com.topjava.votesystem.controller.restaurant;

import com.topjava.votesystem.model.Restaurant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = RestaurantUIController.REST_URL)
public class RestaurantUIController extends AbstractRestaurantController {

    static final String REST_URL = "/restaurants";
    static final String LIST_RESTAURANTS = "restaurants/restaurants";
    static final String ADD_RESTAURANT = "restaurants/add-restaurant";
    static final String UPDATE_RESTAURANT = "restaurants/update-restaurant";


    @GetMapping
    public String showRestaurantsPage(Model model) {
        model.addAttribute("restaurants", super.getAll());
        return LIST_RESTAURANTS;
    }

    @GetMapping("/add")
    public String showAddRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return ADD_RESTAURANT;
    }

    @GetMapping("/update/{id}")
    public String showUpdateRestaurantForm(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", super.get(id));
        return UPDATE_RESTAURANT;
    }
}

