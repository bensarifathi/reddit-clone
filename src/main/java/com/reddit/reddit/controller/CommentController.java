package com.reddit.reddit.controller;

import com.reddit.reddit.dto.CommentDto;
import com.reddit.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity
                .status(CREATED)
                .body(commentService.createComment(commentDto));
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity
                .status(OK)
                .body(commentService.getCommentsByPost(postId));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<CommentDto>> getCommentsByUsername(@PathVariable String username) {
        return ResponseEntity
                .status(OK)
                .body(commentService.getCommentsByUser(username));
    }
}
