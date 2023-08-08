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
     * Создание нового блюда
     * @param dish - блюдо, которое будем добавлять
     * @return созданное блюдо
     */
    @Transactional
    public Dish create(Dish dish) {
        log.info("create {}", dish);
        return repository.save(dish);
    }

    /**
     * Получение всех блюд
     * @return список всех блюд, без исключений
     */
    public List<Dish> readAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * Получить ресторан по id
     * @param id - идентификатор
     * @return блюдо
     */
    public Dish read(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Обновить блюдо по id
     * @param dish - блюдо
     * @param id - идентификатор
     */
    public void update(Dish dish, Long id) {
        dish.setId(id);
        log.info("update {} with id={}", dish, id);
        repository.save(dish);
    }

    /**
     * Удалить блюдо по id
     * @param id - идентификатор
     */
    public void delete(Long id) {
        log.info("delete {}", id);
        repository.deleteById(id);
    }

    public void deleteByRestaurant(Restaurant restaurant) {
        log.info("delete by restaurant = {}", restaurant);
        if (!restaurant.getDishes().isEmpty()) {
            repository.deleteDishesByRestaurant(restaurant);
        }

    }




}
