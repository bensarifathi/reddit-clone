package com.reddit.reddit.mapper;

import com.reddit.reddit.dto.RegisterRequestDto;
import com.reddit.reddit.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserMapper {
    public User toModel(RegisterRequestDto registerRequestDto) {
        var user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setCreatedAt(Instant.now());
        user.setEnabled(false);
        return user;
    }
}
