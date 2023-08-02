package com.topjava.votesystem.controller;

import com.topjava.votesystem.service.VoteService;
import com.topjava.votesystem.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/votes")
public class VoteController extends BaseController {

    private final VoteService voteService;
    public final String VOTE_TEMPLATE = "admin/votes";

    @GetMapping
    public String votes(Model model) {
        model.addAttribute("votes", voteService.readAll());
        return VOTE_TEMPLATE;
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        voteService.delete(ObjectUtil.getId(request));
        return "redirect:/votes";
    }
}
