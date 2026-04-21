package com.ecotrack.livepoll.repository;

import com.ecotrack.livepoll.model.Poll;
import com.ecotrack.livepoll.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPollIdAndUserId(Long pollId, Long userId);
    long countByPoll(Poll poll);
}
