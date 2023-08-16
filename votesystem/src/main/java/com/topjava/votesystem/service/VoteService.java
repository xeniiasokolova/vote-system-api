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

    public List<Vote> getAll() {
        return voteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Vote get(Long id) {
        return voteRepository.findById(id).get();
    }


    public void update(Vote vote, Long id) {
        vote.setId(id);
        voteRepository.save(vote);
    }

    public void delete(Long id) {
        voteRepository.deleteById(id);
    }

    public void vote(Vote vote) {
        User user = vote.getUser();
        if (isUpdatedVote(user)) {
            long voteId = getUserVoteToday(user).get().getId();
            update(vote, voteId);
        } else {
            voteRepository.save(vote);
        }
    }

    public boolean isUpdatedVote(User user) {
        return hasVotedToday(user) && !hasVotedAfter11AM(user);
    }

    public Optional<Vote> getUserVoteToday(User user) {
        LocalDateTime now = LocalDateTime.now();
        return voteRepository.findByUserAndVoteDateTimeBetween(user,
                        now.with(LocalTime.MIN), now.with(LocalTime.MAX))
                .stream()
                .findFirst();
    }

    //если уже голосовал = true
    public boolean hasVotedToday(User user) {
        return getUserVoteToday(user).isPresent();
    }

    //если голосовал и время после 11 часов = true
    public boolean hasVotedAfter11AM(User user) {
        if (hasVotedToday(user)) {
            LocalDateTime limitTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));
            return LocalDateTime.now().isAfter(limitTime);
        }
        return false;
    }


    public void deleteAllVotesByRestaurantId(Long id) {
        if (hasVotesByRestaurantId(id)) {
            voteRepository.deleteAllByRestaurantId(id);
        }
    }

    public boolean hasVotesByRestaurantId(Long id) {
        return !voteRepository.findVotesByRestaurantId(id).isEmpty();
    }

    public void deleteAllVotesByDishId(Long id) {
        if (hasVotesByDishId(id)) {
            voteRepository.deleteAllByDishId(id);
        }
    }

    public boolean hasVotesByDishId(Long id) {
        return !voteRepository.findVotesByDishId(id).isEmpty();
    }

}
