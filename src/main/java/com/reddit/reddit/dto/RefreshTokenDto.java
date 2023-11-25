package com.reddit.reddit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenDto {
    @NotBlank(message = "refresh token cannot be empty")
    private String refreshToken;
    private String username;
}
