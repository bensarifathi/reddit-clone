package com.reddit.reddit.service;

import com.reddit.reddit.dto.VoteDto;
import com.reddit.reddit.exceptions.SpringRedditException;
import com.reddit.reddit.mapper.VoteMapper;
import com.reddit.reddit.model.NotificationEmail;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import com.reddit.reddit.model.Vote;
import com.reddit.reddit.repository.PostRepository;
import com.reddit.reddit.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.reddit.reddit.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    @Transactional
    public void vote(VoteDto voteDto) {
        var user = authService.getCurrentUser();
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post, user);
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(voteMapper.toVote(voteDto, post, user));
        postRepository.save(post);
        var content = mailContentBuilder.build(user.getUsername() + " has add new vote to your post " + post.getTitle() + ".");
        sendNotificationEmail(content, post.getUser());
    }

    private void sendNotificationEmail(String content, User user) {
        mailService.SendMail(new NotificationEmail(
                "New vote on your post",
                user.getEmail(),
                content
        ));
    }
}
