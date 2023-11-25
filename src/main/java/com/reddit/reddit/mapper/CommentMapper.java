package com.reddit.reddit.mapper;

import com.reddit.reddit.dto.CommentDto;
import com.reddit.reddit.model.Comment;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toComment(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "createdDate", source = "createdAt")
    CommentDto toDto(Comment comment);
}
