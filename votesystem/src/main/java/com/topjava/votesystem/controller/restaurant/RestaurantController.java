package com.topjava.votesystem.controller.restaurant;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.model.Role;
import com.topjava.votesystem.service.RestaurantService;
import com.topjava.votesystem.service.UserService;
import com.topjava.votesystem.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
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
    public String readAll(Model model, Principal principal) {
        String roleCurrentUser = userService.getRoleByUsername(principal.getName());
        model.addAttribute("restaurants",
                restaurantService.readAll());
        if (roleCurrentUser.equals(Role.ADMIN.name())) {
            return TEMPLATE_ADMIN_RESTAURANT;
        }
        return TEMPLATE_USER_RESTAURANT;
    }

    //добавить новый ресторан
    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return TEMPLATE_ADMIN_RESTAURANT_FORM;
    }

    //обновить данные по ресторану
    @GetMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        model.addAttribute("restaurant", restaurantService.read(ObjectUtil.getId(request)));
        return TEMPLATE_ADMIN_RESTAURANT_FORM;
    }

    //coхранить изменения по ресторану
    @PostMapping(value = "/save")
    public String save(HttpServletRequest request) {
        Restaurant restaurant = new Restaurant(request.getParameter("name"));
        if (request.getParameter("id").isEmpty()) {
            restaurantService.create(restaurant);
        } else {
            restaurantService.update(restaurant, ObjectUtil.getId(request));
        }
        return "redirect:/restaurants";
    }

    //удалить данные
    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        restaurantService.delete(ObjectUtil.getId(request));
        return "redirect:/restaurants";
    }
}

