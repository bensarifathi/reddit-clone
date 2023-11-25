package com.reddit.reddit.service;

import com.reddit.reddit.dto.AuthenticationResponse;
import com.reddit.reddit.dto.LoginRequestDto;
import com.reddit.reddit.dto.RefreshTokenDto;
import com.reddit.reddit.dto.RegisterRequestDto;
import com.reddit.reddit.exceptions.SpringRedditException;
import com.reddit.reddit.mapper.UserMapper;
import com.reddit.reddit.model.NotificationEmail;
import com.reddit.reddit.model.User;
import com.reddit.reddit.model.VerificationToken;
import com.reddit.reddit.repository.RefreshTokenRepository;
import com.reddit.reddit.repository.UserRepository;
import com.reddit.reddit.repository.VerificationTokenRepository;
import com.reddit.reddit.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public User getCurrentUser() {

        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getByEmail(email).orElseThrow(
                () -> new SpringRedditException("can't fetch user")
        );
    }
    @Transactional
    public void signUp(RegisterRequestDto registerRequestDto) {
        var user = userMapper.toModel(registerRequestDto);
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        try {
            userRepository.save(user);
            var token = generateVerificationToken(user);
            mailService.SendMail(new NotificationEmail("Please activate your account",
                    user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                    "please click on the below url to activate your account : " +
                    "http://localhost:8080/api/auth/accountVerification/" + token));
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("unique_mail_per_user")) {
                throw new SpringRedditException("Email already exist");
            }
            throw e;
        }

    }

    private String generateVerificationToken(User user) {
        var token = UUID.randomUUID().toString();
        var verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Token Invalid"));
        fetchUserAndEnable(verificationToken);
    }
    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        var user = userRepository
                .getByEmail(email)
                .orElseThrow(() -> new SpringRedditException("User not found with email - " + email));
        user.setEnabled(true);
        userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequestDto loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtProvider.generateToken(authenticate);
            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plus(jwtProvider.getJwtExpirationInSecond(), ChronoUnit.SECONDS))
                    .username(loginRequest.getEmail())
                    .build();
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenDto refreshTokenDto) {
        refreshTokenService.validateRefreshToken(refreshTokenDto.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenDto.getUsername());
        return AuthenticationResponse.builder()
                .refreshToken(refreshTokenDto.getRefreshToken())
                .authenticationToken(token)
                .expiresAt(Instant.now().plus(jwtProvider.getJwtExpirationInSecond(), ChronoUnit.SECONDS))
                .username(refreshTokenDto.getUsername())
                .build();
    }
}
