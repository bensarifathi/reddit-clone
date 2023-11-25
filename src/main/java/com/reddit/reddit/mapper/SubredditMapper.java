package com.reddit.reddit.mapper;

import com.reddit.reddit.dto.SubredditDto;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.Subreddit;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubredditMapper {
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto toSubredditDto(Subreddit subreddit);
    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit toSubreddit(SubredditDto subredditDto);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }
}
