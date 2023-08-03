package com.topjava.votesystem.controller.restaurant;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.model.Role;
import com.topjava.votesystem.service.RestaurantService;
import com.topjava.votesystem.service.UserService;
import com.topjava.votesystem.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController extends BaseController {
    public static final String TEMPLATE_USER_RESTAURANT = "user/restaurants";
    public static final String TEMPLATE_ADMIN_RESTAURANT = "admin/restaurants";
    public static final String TEMPLATE_ADMIN_RESTAURANT_FORM = "admin/restaurantForm";

    private final RestaurantService restaurantService;
    private final UserService userService;


    //получение списка всех ресторанов
    @GetMapping
    public ModelAndView readAll(Model model, Principal principal) {
        String roleCurrentUser = userService.getRoleByUsername(principal.getName());
        model.addAttribute("restaurants",
                restaurantService.readAll());
        if (roleCurrentUser.equals(Role.ADMIN.name())) {
            return new ModelAndView(TEMPLATE_ADMIN_RESTAURANT);
        }
        return new ModelAndView(TEMPLATE_USER_RESTAURANT);
    }

    //добавить новый ресторан
    @GetMapping("/add")
    public ModelAndView create(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return new ModelAndView(TEMPLATE_ADMIN_RESTAURANT_FORM);
    }

    //обновить данные по ресторану
    @GetMapping("/update")
    public ModelAndView update(HttpServletRequest request, Model model) {
        model.addAttribute("restaurant", restaurantService.read(ObjectUtil.getId(request)));
        return new ModelAndView(TEMPLATE_ADMIN_RESTAURANT_FORM);
    }

    //coхранить изменения по ресторану
    @PostMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request) {
        Restaurant restaurant = new Restaurant(request.getParameter("name"));
        if (request.getParameter("id").isEmpty()) {
            restaurantService.create(restaurant);
        } else {
            restaurantService.update(restaurant, ObjectUtil.getId(request));
        }
        return new ModelAndView("redirect:/restaurants");
    }

    //удалить данные
    @GetMapping("/delete")
    public ModelAndView delete(HttpServletRequest request) {
        restaurantService.delete(ObjectUtil.getId(request));
        return new ModelAndView("redirect:/restaurants");
    }
}

