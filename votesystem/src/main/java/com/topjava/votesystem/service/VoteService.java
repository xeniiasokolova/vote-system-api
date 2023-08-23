package com.topjava.votesystem.service;

import com.topjava.votesystem.model.User;
import com.topjava.votesystem.model.Vote;
import com.topjava.votesystem.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@AllArgsConstructor
public class VoteService {

    @Autowired
    private final VoteRepository voteRepository;

    @Transactional
    public Vote create(Vote vote) {
        return voteRepository.save(vote);
    }

    /**
     * Get all votes
     *
     * @return list of all votes
     */
    public List<Vote> getAll() {
        return voteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Get vote by id
     *
     * @param id - vote's identity
     * @return vote
     */
    public Vote get(Long id) {
        return voteRepository.findById(id).get();
    }

    /**
     * Update vote
     *
     * @param vote - vote to be updated
     * @param id   - vote's identity
     */
    public void update(Vote vote, Long id) {
        vote.setId(id);
        voteRepository.save(vote);
    }

    /**
     * Delete vote
     *
     * @param id - vote's identity
     */
    public void delete(Long id) {
        voteRepository.deleteById(id);
    }

    /**
     * If the vote exists, then update it, otherwise create a new one
     *
     * @param vote - vote to be created or updated
     */
    public void vote(Vote vote) {
        User user = vote.getUser();
        if (hasVotedToday(user)) {
            long voteId = getUserVoteToday(user).get().getId();
            update(vote, voteId);
        } else {
            voteRepository.save(vote);
        }
    }

    /**
     * if user vote today get his vote
     *
     * @param user - user
     * @return vote
     */
    public Optional<Vote> getUserVoteToday(User user) {
        LocalDateTime now = LocalDateTime.now();
        return voteRepository.findByUserAndVoteDateTimeBetween(user,
                        now.with(LocalTime.MIN), now.with(LocalTime.MAX))
                .stream()
                .findFirst();
    }


    /**
     * Check user is voted today or not
     *
     * @param user - user to check vote
     * @return true if user voted today
     */
    public boolean hasVotedToday(User user) {
        return getUserVoteToday(user).isPresent();
    }

    /**
     * User can change vote until 11 am
     *
     * @param user - user to be updated vote
     * @return true if user can change vote
     */
    public boolean canUserChangeVote(User user) {

        if (hasVotedToday(user)) {
            LocalDateTime limitTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));
            return LocalDateTime.now().isAfter(limitTime);
        }
        return false;

    }

    /**
     * Remove all votes by restaurant id
     *
     * @param id - restaurant identity
     */
    public void deleteAllVotesByRestaurantId(Long id) {
        if (hasVotesByRestaurantId(id)) {
            voteRepository.deleteAllByRestaurantId(id);
        }
    }

    /**
     * Remove all votes by user id
     *
     * @param id - user's identity
     */
    public void deleteAllVotesByUserId(Long id) {
        if (hasVotesByUserId(id)) {
            voteRepository.deleteAllByUserId(id);
        }
    }

    /**
     * Check user is voted today or not by user id
     *
     * @param id - user's identity
     * @return true if user voted today
     */
    public boolean hasVotesByUserId(Long id) {
        return !voteRepository.findVotesByUserId(id).isEmpty();
    }

    /**
     * Get all restaurant's dishes
     *
     * @param id - restaurant identity
     * @return list of dishes
     */
    public boolean hasVotesByRestaurantId(Long id) {
        return !voteRepository.findVotesByRestaurantId(id).isEmpty();
    }

    /**
     * Remove all votes by dish id
     *
     * @param id - dish's id
     */
    public void deleteAllVotesByDishId(Long id) {
        if (hasVotesByDishId(id)) {
            voteRepository.deleteAllByDishId(id);
        }
    }

    /**
     * Check user is voted today or not by dish id
     *
     * @param id - dish's identity
     * @return true if user voted today
     */
    public boolean hasVotesByDishId(Long id) {
        return !voteRepository.findVotesByDishId(id).isEmpty();
    }

}
