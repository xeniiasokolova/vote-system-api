package com.topjava.votesystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    public static final int START_SEQ_RESTAURANT = 100000;
    @Id
    @SequenceGenerator(name = "global_seq_restaurants",
            sequenceName = "global_seq_restaurants",
            allocationSize = 1,
            initialValue = START_SEQ_RESTAURANT)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_restaurants")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    private LocalDateTime registered = LocalDateTime.now();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonManagedReference
    private List<Dish> dishes;

    public Restaurant() {
    }

    public Restaurant(String name) {
        this.name = name;
        this.registered = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", registered=" + registered.toString() +
                ", dishes=" + dishes.toString() +
                '}';
    }
}

