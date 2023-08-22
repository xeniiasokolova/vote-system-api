package com.topjava.votesystem.repository;

import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Dish, Long> {

    void deleteDishesByRestaurant(Restaurant restaurant);

    List<Dish> getDishesByRestaurantId(Long id);
}
