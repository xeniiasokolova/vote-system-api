package com.topjava.votesystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "dishes")
public class Dish {
    public static final int START_SEQ_DISH = 100000;

    @Id
    @SequenceGenerator(name = "global_seq_dishes", sequenceName = "global_seq_dishes", allocationSize = 1, initialValue = START_SEQ_DISH)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_dishes")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Restaurant restaurant;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "registered")
    private LocalDateTime registered;


    public Dish() {

    }

    public Dish(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Dish(Restaurant restaurant, String name, String description, Float price) {
        this.name = name;
        this.restaurant = restaurant;
        this.description = description;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public boolean isNew() {
        return this.id == null;
    }



    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", restaurant=" + restaurant +
                ", description='" + description + '\'' +
                ", price=" + price.toString() +
                ", registered=" + registered.toString() +
                '}';
    }


}
