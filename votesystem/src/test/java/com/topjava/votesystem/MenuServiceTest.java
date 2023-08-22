package com.topjava.votesystem;

import com.topjava.votesystem.model.Dish;
import com.topjava.votesystem.repository.MenuRepository;
import com.topjava.votesystem.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MenuServiceTest {

    @Mock
    private MenuRepository repository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateDish() {
        Dish dish = new Dish();
        when(repository.save(any(Dish.class))).thenReturn(dish);
        Dish createdDish = menuService.create(dish);
        assertNotNull(createdDish);
    }

    @Test
    public void testGetAllDishes() {
        List<Dish> dishList = new ArrayList<>();
        dishList.add(new Dish());
        dishList.add(new Dish());
        when(repository.findAll(any(Sort.class))).thenReturn(dishList);
        List<Dish> allDishes = menuService.getAll();
        assertEquals(2, allDishes.size());
    }

    @Test
    public void testGetDishById() {
        Long dishId = 1L;
        Dish dish = new Dish();
        dish.setId(dishId);
        when(repository.findById(dishId)).thenReturn(Optional.of(dish));
        Dish retrievedDish = menuService.get(dishId);
        assertNotNull(retrievedDish);
        assertEquals(dishId, retrievedDish.getId());
    }

    @Test
    public void testUpdateDish_Success() {
        Long dishId = 1L;
        Dish existingDish = new Dish();
        existingDish.setId(dishId);
        existingDish.setName("Old Name");

        Dish updatedDish = new Dish();
        updatedDish.setName("New Name");

        when(repository.findById(dishId)).thenReturn(Optional.of(existingDish));
        when(repository.save(any(Dish.class))).thenReturn(updatedDish);

        boolean result = menuService.update(updatedDish, dishId);

        assertTrue(result);
        assertEquals("New Name", existingDish.getName());
    }

    @Test
    public void testUpdateDish_Failure() {
        Long dishId = 1L;
        Dish updatedDish = new Dish();
        when(repository.findById(dishId)).thenReturn(Optional.empty());
        boolean result = menuService.update(updatedDish, dishId);
        assertFalse(result);
    }

    @Test
    public void testDeleteDish() {
        Long dishId = 1L;
        menuService.delete(dishId);
        verify(repository, times(1)).deleteById(dishId);
    }

    @Test
    public void testGetDishesByRestaurantId() {
        Long restaurantId = 1L;
        List<Dish> dishList = new ArrayList<>();
        dishList.add(new Dish());
        dishList.add(new Dish());
        when(repository.getDishesByRestaurantId(restaurantId)).thenReturn(dishList);
        List<Dish> dishesByRestaurant = menuService.getDishesByRestaurantId(restaurantId);
        assertEquals(2, dishesByRestaurant.size());
    }
}