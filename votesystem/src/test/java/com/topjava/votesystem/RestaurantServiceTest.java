package com.topjava.votesystem;

import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.repository.RestaurantRepository;
import com.topjava.votesystem.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RestaurantServiceTest {
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void createRestaurant() {
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant createdRestaurant = restaurantService.create(restaurant);

        assertNotNull(createdRestaurant);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void readAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        when(restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(restaurants);

        List<Restaurant> retrievedRestaurants = restaurantService.readAll();

        assertEquals(restaurants, retrievedRestaurants);
        verify(restaurantRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    void readRestaurantById() {
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant retrievedRestaurant = restaurantService.read(1L);

        assertNotNull(retrievedRestaurant);
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void checkIfRestaurantExists() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new Restaurant()));

        boolean exists = restaurantService.isRestaurantExist(1L);

        assertTrue(exists);
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void updateRestaurant() {
        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setId(1L);
        existingRestaurant.setName("Old Name");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existingRestaurant));
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(1L);
        updatedRestaurant.setName("New Name");
        boolean isUpdated = restaurantService.update(updatedRestaurant, 1L);
        assertTrue(isUpdated);
        assertEquals("New Name", existingRestaurant.getName());
        verify(restaurantRepository, times(1)).save(existingRestaurant);
    }

    @Test
    void deleteRestaurant() {
        Long restaurantId = 1L;
        doNothing().when(restaurantRepository).deleteById(restaurantId);

        restaurantService.delete(restaurantId);

        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }
}
