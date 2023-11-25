package com.reddit.reddit.repository;

import com.reddit.reddit.dto.CommentDto;
import com.reddit.reddit.model.Comment;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);
}
