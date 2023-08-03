package com.topjava.votesystem.controller;

import com.topjava.votesystem.service.VoteService;
import com.topjava.votesystem.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/votes")
public class VoteController extends BaseController {

    private final VoteService voteService;
    public final String VOTE_TEMPLATE = "admin/votes";

    @GetMapping
    public ModelAndView votes(Model model) {
        model.addAttribute("votes", voteService.readAll());
        return new ModelAndView(VOTE_TEMPLATE);
    }

    @GetMapping("/delete")
    public ModelAndView delete(HttpServletRequest request) {
        voteService.delete(ObjectUtil.getId(request));
        return new ModelAndView("redirect:/votes");
    }
}
