package com.reddit.reddit.controller;

import com.reddit.reddit.dto.PostRequestDto;
import com.reddit.reddit.dto.PostResponseDto;
import com.reddit.reddit.service.PostService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity
                .status(CREATED)
                .body(postService.createPost(postRequestDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponseDto>> listPosts() {
        return ResponseEntity
                .status(OK)
                .body(postService.listPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity
                .status(OK)
                .body(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
