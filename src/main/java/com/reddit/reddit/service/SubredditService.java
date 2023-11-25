package com.reddit.reddit.service;

import com.reddit.reddit.dto.SubredditDto;
import com.reddit.reddit.exceptions.SpringRedditException;
import com.reddit.reddit.mapper.SubredditMapper;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubRedditRepository subRedditRepository;
    private final SubredditMapper subredditMapper;
    public SubredditDto createSubreddit(SubredditDto subredditDto) {
        Subreddit subreddit = subredditMapper.toSubreddit(subredditDto);
        subRedditRepository.save(subreddit);
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }


    public List<SubredditDto> getAllSubreddit() {
        return subRedditRepository
                .findAll()
                .stream()
                .map(subredditMapper::toSubredditDto)
                .collect(Collectors.toList());
    }



    public SubredditDto getSubredditById(Long id) {
        var subReddit = subRedditRepository
                .findById(id)
                .orElseThrow(() -> new SpringRedditException("Reddit with id: " + id + " not found"));
        return subredditMapper.toSubredditDto(subReddit);
    }

    public void deleteSubreddit(Long id) {
        subRedditRepository.deleteById(id);
    }
}
