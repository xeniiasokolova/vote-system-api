package com.topjava.votesystem.repository;

import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuRepository extends JpaRepository<Dish, Long> {

    void deleteDishesByRestaurant(Restaurant restaurant);
}
