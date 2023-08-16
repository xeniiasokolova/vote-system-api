package com.topjava.votesystem.controller.votes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = VoteUIController.REST_URL)
public class VoteUIController extends VoteAbstractController {
    static final String REST_URL = "/votes";

    @GetMapping
    public String showVotesPage(Model model) {
        model.addAttribute("votes", super.getAll());
        return "votes-list";
    }
}
