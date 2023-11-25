package com.reddit.reddit.service;

import com.reddit.reddit.dto.PostRequestDto;
import com.reddit.reddit.dto.PostResponseDto;
import com.reddit.reddit.exceptions.SpringRedditException;
import com.reddit.reddit.mapper.PostMapper;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.model.User;
import com.reddit.reddit.repository.PostRepository;
import com.reddit.reddit.repository.SubRedditRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubRedditRepository subRedditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Subreddit subreddit = subRedditRepository
                .findByName(postRequestDto.getSubredditName())
                .orElseThrow(
                        () ->
                                new SpringRedditException("No subreddit found with name " + postRequestDto.getSubredditName())
                );
        User user = authService.getCurrentUser();
        var post = postMapper.toPost(postRequestDto, subreddit, user);
        post.setCreatedAt(Instant.now());
        return postMapper.mapToDto(postRepository.save(post));
    }

    public List<PostResponseDto> listPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(postMapper::mapToDto)
                .toList();
    }

    public PostResponseDto getPostById(Long id) {

        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new SpringRedditException("Post not found"));
        return postMapper.mapToDto(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
