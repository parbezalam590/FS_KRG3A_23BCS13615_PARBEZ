package com.ecotrack.livepoll.repository;

import com.ecotrack.livepoll.model.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			update PollOption o
			set o.voteCount = o.voteCount + 1
			where o.id = :optionId and o.poll.id = :pollId
			""")
	int incrementVoteCount(@Param("pollId") Long pollId, @Param("optionId") Long optionId);
}
