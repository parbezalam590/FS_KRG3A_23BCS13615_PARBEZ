package com.ecotrack.livepoll.repository;

import com.ecotrack.livepoll.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
