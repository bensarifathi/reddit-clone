package com.reddit.reddit.mapper;

import com.reddit.reddit.dto.PostRequestDto;
import com.reddit.reddit.dto.PostResponseDto;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.model.User;
import com.reddit.reddit.repository.CommentRepository;
import com.reddit.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected VoteRepository voteRepository;


    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequestDto.description")
    @Mapping(target = "title", source = "postRequestDto.postName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    public abstract Post toPost(PostRequestDto postRequestDto, Subreddit subreddit, User user);

    @Mapping(target = "postName", source = "title")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "voteCount", expression = "java(getVoteCount(post))")
    @Mapping(target = "duration", source = "createdAt")
    public abstract PostResponseDto mapToDto(Post post);

    protected Integer getCommentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    protected Integer getVoteCount(Post post) {
        return voteRepository.findByPost(post).size();
    }
}
