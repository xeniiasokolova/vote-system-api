package com.topjava.votesystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "votes")
public class Vote {

    public static final int START_SEQ_VOTE = 100000;
    @Id
    @SequenceGenerator(name = "global_seq_votes", sequenceName = "global_seq_votes", allocationSize = 1, initialValue = START_SEQ_VOTE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_votes")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private LocalDateTime voteDateTime;

    public Vote() {

    }

    public Vote(User user, Restaurant restaurant, Dish dish) {
        this.user = user;
        this.restaurant = restaurant;
        this.dish = dish;
        this.voteDateTime = LocalDateTime.now();
    }
}
