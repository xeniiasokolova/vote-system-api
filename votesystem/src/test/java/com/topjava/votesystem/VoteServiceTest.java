package com.topjava.votesystem;

import com.topjava.votesystem.model.User;
import com.topjava.votesystem.model.Vote;
import com.topjava.votesystem.repository.VoteRepository;
import com.topjava.votesystem.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {
    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateVote() {
        Vote vote = new Vote();
        when(voteRepository.save(vote)).thenReturn(vote);
        Vote createdVote = voteService.create(vote);
        assertEquals(vote, createdVote);
        verify(voteRepository, times(1)).save(vote);
    }

    @Test
    void testUpdateVote() {
        Vote vote = new Vote();
        long voteId = 1L;
        when(voteRepository.save(vote)).thenReturn(vote);
        voteService.update(vote, voteId);
        assertEquals(voteId, vote.getId());
        verify(voteRepository, times(1)).save(vote);
    }

    @Test
    void testDeleteVote() {
        long voteId = 1L;
        voteService.delete(voteId);
        verify(voteRepository, times(1)).deleteById(voteId);
    }

    @Test
    void testVote() {
        User user = new User();
        user.setId(1L);
        Vote vote = new Vote();
        vote.setUser(user);
        when(voteRepository.findByUserAndVoteDateTimeBetween(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        voteService.vote(vote);
        verify(voteRepository, times(1)).save(vote);
    }


    @Test
    void testHasVotesByRestaurantId() {
        long restaurantId = 1L;
        when(voteRepository.findVotesByRestaurantId(anyLong())).thenReturn(new ArrayList<>());
        boolean hasVotes = voteService.hasVotesByRestaurantId(restaurantId);
        assertFalse(hasVotes);
        verify(voteRepository, times(1)).findVotesByRestaurantId(restaurantId);
    }

    @Test
    void testHasVotesByDishId() {
        long dishId = 1L;
        when(voteRepository.findVotesByDishId(anyLong())).thenReturn(new ArrayList<>());
        boolean hasVotes = voteService.hasVotesByDishId(dishId);
        assertFalse(hasVotes);
        verify(voteRepository, times(1)).findVotesByDishId(dishId);
    }

}
