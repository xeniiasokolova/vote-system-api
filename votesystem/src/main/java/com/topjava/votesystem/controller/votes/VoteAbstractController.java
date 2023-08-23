package com.topjava.votesystem.controller.votes;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.Vote;
import com.topjava.votesystem.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VoteAbstractController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService service;

    /**
     * Get vote by id
     *
     * @param id - vote's identity
     * @return vote
     */
    public Vote get(long id) {
        log.info("get vote {}", id);
        return service.get(id);
    }

    /**
     * Delete vote
     *
     * @param id - vote's identity
     */
    public void delete(long id) {
        log.info("delete vote {}", id);
        service.delete(id);
    }

    /**
     * Get all votes
     *
     * @return list of all votes
     */
    public List<Vote> getAll() {
        log.info("get all votes");
        return service.getAll();
    }
}
