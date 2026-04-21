package com.ecotrack.livepoll.service;

import com.ecotrack.livepoll.dto.PollDtos;
import com.ecotrack.livepoll.model.AppUser;
import com.ecotrack.livepoll.model.Poll;
import com.ecotrack.livepoll.model.PollOption;
import com.ecotrack.livepoll.model.Vote;
import com.ecotrack.livepoll.repository.PollOptionRepository;
import com.ecotrack.livepoll.repository.PollRepository;
import com.ecotrack.livepoll.repository.UserRepository;
import com.ecotrack.livepoll.repository.VoteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public PollService(PollRepository pollRepository,
                       PollOptionRepository pollOptionRepository,
                       VoteRepository voteRepository,
                       UserRepository userRepository) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<PollDtos.PollResponse> getAllPolls() {
        return pollRepository.findAllWithOptionsOrderByCreatedAtDesc().stream().map(this::mapPoll).toList();
    }

    @Transactional(readOnly = true)
    public PollDtos.PollResponse getPollById(Long id) {
        Poll poll = pollRepository.findWithOptionsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found"));
        return mapPoll(poll);
    }

    @Transactional
    public PollDtos.PollResponse createPoll(PollDtos.CreatePollRequest request, String creatorEmail) {
        Poll poll = new Poll();
        poll.setQuestion(request.question());
        poll.setCreatedBy(creatorEmail);

        List<PollOption> options = request.options().stream()
                .map(text -> {
                    PollOption option = new PollOption();
                    option.setPoll(poll);
                    option.setOptionText(text);
                    return option;
                })
                .toList();

        poll.setOptions(options);
        Poll saved = pollRepository.save(poll);
        return mapPoll(saved);
    }

    @Transactional
    public PollDtos.PollResponse vote(Long pollId, Long optionId, String voterEmail) {
        Poll poll = pollRepository.findWithOptionsById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found"));

        if (poll.isClosed()) {
            throw new IllegalStateException("Poll is closed");
        }

        AppUser voter = userRepository.findByEmail(voterEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (voteRepository.findByPollIdAndUserId(pollId, voter.getId()).isPresent()) {
            throw new IllegalStateException("You have already voted on this poll");
        }

        int rowsUpdated = pollOptionRepository.incrementVoteCount(pollId, optionId);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Option does not belong to this poll");
        }

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(voter);
        vote.setSelectedOptionId(optionId);

        try {
            voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("You have already voted on this poll");
        }

        return mapPoll(pollRepository.findWithOptionsById(pollId).orElseThrow());
    }

    @Transactional
    public PollDtos.PollResponse closePoll(Long pollId) {
        Poll poll = pollRepository.findWithOptionsById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found"));
        poll.setClosed(true);
        return mapPoll(pollRepository.save(poll));
    }

    @Transactional
    public void deletePoll(Long pollId) {
        pollRepository.deleteById(pollId);
    }

    private PollDtos.PollResponse mapPoll(Poll poll) {
        List<PollDtos.PollOptionResponse> options = poll.getOptions().stream()
                .map(o -> new PollDtos.PollOptionResponse(o.getId(), o.getOptionText(), o.getVoteCount()))
                .toList();

        return new PollDtos.PollResponse(
                poll.getId(),
                poll.getQuestion(),
                poll.getCreatedAt(),
                poll.getCreatedBy(),
                poll.isClosed(),
                options
        );
    }
}
