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
    @SequenceGenerator(name = "global_seq_restaurants", sequenceName = "global_seq_restaurants", allocationSize = 1, initialValue = START_SEQ_RESTAURANT)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_restaurants")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "voices")
    private int countVoices = 0;

    @Column(name = "datetime_last_vote", columnDefinition = "timestamp default now()")
    private LocalDateTime dateTimeLastVote;

    @Column(name = "registered")
    private LocalDateTime registered;

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

    public int getCountVoices() {
        return countVoices;
    }

    public void setCountVoices(int countVoices) {
        this.countVoices = countVoices;
    }

    public LocalDateTime getDateTimeLastVote() {
        return dateTimeLastVote;
    }

    public void setDateTimeLastVote(LocalDateTime dateTimeLastVote) {
        this.dateTimeLastVote = dateTimeLastVote;
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
}

