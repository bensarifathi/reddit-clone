package com.reddit.reddit.service;

import com.reddit.reddit.dto.CommentDto;
import com.reddit.reddit.exceptions.SpringRedditException;
import com.reddit.reddit.mapper.CommentMapper;
import com.reddit.reddit.model.NotificationEmail;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import com.reddit.reddit.repository.CommentRepository;
import com.reddit.reddit.repository.PostRepository;
import com.reddit.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {
    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    public CommentDto createComment(CommentDto commentDto) {
        var user = authService.getCurrentUser();
        var post = postRepository.getReferenceById(commentDto.getPostId());
        var comment = commentMapper.toComment(
                commentDto,
                post,
                user
        );
        comment.setCreatedAt(Instant.now());
        commentDto = commentMapper.toDto(commentRepository.save(comment));
        var mailContent = mailContentBuilder.build(comment.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(mailContent, post.getUser());
        return commentDto;
    }

    private void sendCommentNotification(String mailContent, User user) {
        mailService.SendMail(new NotificationEmail(
                user.getUsername() + " Commented on your post",
                user.getEmail(),
                mailContent
        ));
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        return commentRepository
                .findByPost(post)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public List<CommentDto> getCommentsByUser(String username) {
        var user = userRepository.getByEmail(username)
                .orElseThrow(() -> new SpringRedditException("User not found"));
        return commentRepository.findByUser(user)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }
}
