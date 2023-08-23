package com.topjava.votesystem.repository;

import com.topjava.votesystem.model.User;
import com.topjava.votesystem.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserAndVoteDateTimeBetween(User user, LocalDateTime todayStart, LocalDateTime todayEnd);

    void deleteAllByRestaurantId(Long id);

    List<Vote> findVotesByRestaurantId(Long id);

    void deleteAllByDishId(Long id);

    List<Vote> findVotesByDishId(Long id);

    void deleteAllByUserId(Long id);

    List<Vote> findVotesByUserId(Long id);

}
