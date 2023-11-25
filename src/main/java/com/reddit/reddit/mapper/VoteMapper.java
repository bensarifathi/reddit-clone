package com.reddit.reddit.mapper;

import com.reddit.reddit.dto.VoteDto;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import com.reddit.reddit.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Vote toVote(VoteDto voteDto, Post post, User user);
}
