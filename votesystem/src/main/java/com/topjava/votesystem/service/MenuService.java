package com.topjava.votesystem.service;


import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MenuService {

    @Autowired
    private final MenuRepository repository;

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    /**
     * Creating a new dish
     *
     * @param dish - dish to be added
     * @return created dish
     */
    @Transactional
    public Dish create(Dish dish) {
        log.info("create {}", dish);
        return repository.save(dish);
    }


    /**
     * Retrieve all dishes
     *
     * @return list of all dishes, no exceptions
     */
    public List<Dish> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * Get restaurant by id
     *
     * @param id - identity
     * @return dish
     */
    public Dish get(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Update dish by id
     *
     * @param dish - dish
     * @param id   - identity
     */
    public boolean update(Dish dish, Long id) {
        log.info("update {} with id={}", dish, id);
        try {
            Dish dishOld = get(id);
            dishOld.setName(dish.getName());
            dishOld.setDescription(dish.getDescription());
            dishOld.setPrice(dish.getPrice());
            repository.save(dishOld);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Remove dish by id
     *
     * @param id - identity
     */
    public void delete(Long id) {
        log.info("delete {}", id);
        repository.deleteById(id);
    }

    /**
     * Remove all dishes by restaurant
     *
     * @param restaurant - restaurant
     */
    public void deleteByRestaurant(Restaurant restaurant) {
        log.info("delete by restaurant = {}", restaurant);
        if (!restaurant.getDishes().isEmpty()) {
            repository.deleteDishesByRestaurant(restaurant);
        }

    }

    /**
     * Get all dishes by restaurant id
     *
     * @param id - identity
     */
    public List<Dish> getDishesByRestaurantId(Long id) {
        return repository.getDishesByRestaurantId(id);
    }

}
