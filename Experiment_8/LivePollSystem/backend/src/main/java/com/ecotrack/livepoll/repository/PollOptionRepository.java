package com.ecotrack.livepoll.repository;

import com.ecotrack.livepoll.model.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
}
