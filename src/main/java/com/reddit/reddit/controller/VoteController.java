package com.reddit.reddit.controller;

import com.reddit.reddit.dto.VoteDto;
import com.reddit.reddit.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public void submitVote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
    }
}
