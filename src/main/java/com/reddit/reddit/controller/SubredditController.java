package com.reddit.reddit.controller;

import com.reddit.reddit.dto.SubredditDto;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping("/")
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity
                .status(CREATED)
                .body(subredditService.createSubreddit(subredditDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<SubredditDto>> getAllSubreddit() {
        return ResponseEntity
                .status(OK)
                .body(subredditService.getAllSubreddit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubredditById(@PathVariable Long id) {
        return ResponseEntity
                .status(OK)
                .body(subredditService.getSubredditById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubreddit(@PathVariable Long id) {
        subredditService.deleteSubreddit(id);
        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

}
