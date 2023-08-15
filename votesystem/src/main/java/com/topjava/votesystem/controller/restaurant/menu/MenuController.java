package com.topjava.votesystem.controller.restaurant.menu;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.*;
import com.topjava.votesystem.service.MenuService;
import com.topjava.votesystem.service.RestaurantService;
import com.topjava.votesystem.service.UserService;
import com.topjava.votesystem.service.VoteService;
import com.topjava.votesystem.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants/{id}/menu")
public class MenuController extends BaseController {
    static final String TEMPLATE_USER_MENU = "user/menu";
    static final String TEMPLATE_ADMIN_MENU = "admin/menu";
    static final String TEMPLATE_ADMIN_MENU_FORM = "admin/menuForm";

    private MenuService service;
    private UserService userService;
    private VoteService voteService;
    private RestaurantService restaurantService;


    //переходим в меню ресторана
    @GetMapping
    public ModelAndView menu(@PathVariable("id") long restaurantId, Principal principal, Model model, HttpServletRequest request) {
        User user = userService.readByUsername(principal.getName());
        Restaurant restaurant = restaurantService.get(restaurantId);

        model.addAttribute("dishes", restaurant.getDishes());
        model.addAttribute("restaurantName", restaurant.getName());

        if (voteService.hasVotedAfter11AM(user)) {
            model.addAttribute("voteError", true);
        }
        if (voteService.hasVotedToday(user)) {
            model.addAttribute("voteConfirmation", true);
        }

        return new ModelAndView(getMenuTemplate(user));
    }

    @PostMapping
    public ModelAndView vote(@RequestParam("confirmationFlag") String confirmationFlag,
                       HttpServletRequest request, Principal principal) {

        User user = userService.readByUsername(principal.getName());
        Dish dish = service.read(ObjectUtil.getId(request));

        if ("true".equals(confirmationFlag) || !voteService.hasVotedToday(user)) {
            voteService.vote(new Vote(user, dish.getRestaurant(), dish));
        }
        return new ModelAndView("redirect:/restaurants/" + dish.getRestaurant().getId() + "/menu");
    }


    @GetMapping("/add")
    public ModelAndView create(@PathVariable("id") Long restaurantId, Model model) {
        Restaurant restaurant = restaurantService.get(restaurantId);
        model.addAttribute("dish", new Dish(restaurant));
        return new ModelAndView(TEMPLATE_ADMIN_MENU_FORM);
    }

    @GetMapping("/update")
    public ModelAndView update(HttpServletRequest request, Model model) {
        model.addAttribute("dish", service.read(ObjectUtil.getId(request)));
        return new ModelAndView(TEMPLATE_ADMIN_MENU_FORM);
    }

    @PostMapping(value = "/save")
    public ModelAndView updateOrDelete(HttpServletRequest request, @PathVariable("id") Long restaurantId) {
        Dish dish = new Dish(restaurantService.get(restaurantId),
                request.getParameter("name"),
                request.getParameter("description"),
                Float.parseFloat(request.getParameter("price")));
        if (request.getParameter("id").isEmpty()) {
            service.create(dish);
        } else {
            service.update(dish, Long.parseLong(request.getParameter("id")));
        }
        return new ModelAndView("redirect:/restaurants/" + restaurantId + "/menu");
    }

    @GetMapping("/delete")
    public ModelAndView delete(HttpServletRequest request, @PathVariable("id") Long restaurantId) {
        service.delete(ObjectUtil.getId(request));
        return new ModelAndView("redirect:/restaurants/" + restaurantId + "/menu");
    }

    private String getMenuTemplate(User user) {
        if (user.getUserRole().equals(Role.ADMIN))
            return TEMPLATE_ADMIN_MENU;
        else return TEMPLATE_USER_MENU;
    }

}