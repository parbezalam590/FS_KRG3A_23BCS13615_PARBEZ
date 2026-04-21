package com.ecotrack.livepoll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;

public class PollDtos {

    public record CreatePollRequest(
            @NotBlank String question,
            @NotEmpty List<String> options
    ) {}

    public record VoteRequest(Long optionId) {}

    public record PollOptionResponse(
            Long id,
            String optionText,
            int voteCount
    ) {}

    public record PollResponse(
            Long id,
            String question,
            Instant createdAt,
            String createdBy,
            boolean closed,
            List<PollOptionResponse> options
    ) {}
}
