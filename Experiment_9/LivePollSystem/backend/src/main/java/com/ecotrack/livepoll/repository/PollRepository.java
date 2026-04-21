package com.ecotrack.livepoll.repository;

import com.ecotrack.livepoll.model.Poll;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {

	@EntityGraph(attributePaths = "options")
	@Query("select distinct p from Poll p left join p.options order by p.createdAt desc")
	List<Poll> findAllWithOptionsOrderByCreatedAtDesc();

	@EntityGraph(attributePaths = "options")
	@Query("select p from Poll p where p.id = :id")
	Optional<Poll> findWithOptionsById(Long id);
}
