package com.topjava.votesystem.service;

import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository repository;

    /**
     * Create new restaurant
     *
     * @param restaurant - restaurant for creation
     * @return saved restaurant
     */
    @Transactional
    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    /**
     * Get all restaurants
     *
     * @return list of all restaurants
     */
    public List<Restaurant> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * Get restaurant by id
     *
     * @param id - identity
     * @return restaurant
     */
    public Restaurant get(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Check restaurant in database
     *
     * @param id - identity
     * @return true - exist, false - not exist
     */
    public boolean isRestaurantExist(long id) {
        try {
            Restaurant restaurant = get(id);
            return restaurant != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update restaurant by id
     *
     * @param restaurant - data for update
     * @param id         - restaurant id for update
     * @return  true - the restaurant is updated, false - not updated
     */
    public boolean update(Restaurant restaurant, long id) {
        try {
            Restaurant restaurantOld = get(id);
            restaurantOld.setName(restaurant.getName());
            repository.save(restaurantOld);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete restaurant by id
     *
     * @param id - identity
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}