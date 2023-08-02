package com.topjava.votesystem.service;


import com.topjava.votesystem.model.Restaurant;
import com.topjava.votesystem.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository repository;
    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    /**
     * Создание нового ресторана
     * @param restaurant - ресторан, который будем добавлять
     * @return сохраненный ресторан
     */
    @Transactional
    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        return repository.save(restaurant);
    }

    /**
     * Получение всех ресторанов
     * @return список всех ресторанов
     */
    public List<Restaurant> readAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * Получить ресторан по id
     * @param id - идентификатор
     * @return ресторан по id
     */
    public Restaurant read(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Обновить ресторан по id
     * @param restaurant - ресторан
     * @param id - идентификатор
     */
    public void update(Restaurant restaurant, Long id) {
        restaurant.setId(id);
        log.info("update {} with id={}", restaurant, id);
        repository.save(restaurant);
    }

    /**
     * Удалить ресторан по id
     * @param id - идентификатор
     */
    public void delete(Long id) {
        log.info("delete {}", id);
        repository.deleteById(id);
    }

}

/*

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    @Autowired
    private final RestaurantRepository repository;
    private Boolean buttonClicked;


    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Restaurant restaurant) {
        repository.save(restaurant);
    }



    public List<Restaurant> search(String keyword) {
        List<Restaurant> restaurants = repository.search(keyword);
        log.info("search {} with keyword={}", restaurants, keyword);
        return restaurants;
    }

    public Boolean isButtonClicked() {
        return buttonClicked;
    }

    public void setButtonClicked() {
        if (buttonClicked == null) {
            buttonClicked = true;
        }
    }

    public void checkForResetRestaurantVotes() {
        List<Restaurant> restaurantsToUpdate = getAll().stream()
                .filter(r -> r.getDateTimeLastVote() != null && r.getDateTimeLastVote().toLocalDate().isBefore(LocalDate.now()))
                .peek(r -> r.setCountVoices(0))
                .collect(Collectors.toList());
        log.info("reset votes from another days {}", restaurantsToUpdate);
        repository.saveAll(restaurantsToUpdate);
    }

    public void deleteVote(Restaurant restaurant) {
        if (restaurant != null && restaurant.getCountVoices() > 0) {
            restaurant.setCountVoices(restaurant.getCountVoices() - 1);
            log.info("delete vote from restaurant {}", restaurant);
            repository.save(restaurant);
        }
    }

    public void addVote(long id) {
        Restaurant restaurant = get(id);
        restaurant.setCountVoices(restaurant.getCountVoices() + 1);
        restaurant.setDateTimeLastVote(LocalDateTime.now());
        repository.save(restaurant);
    }
 */
