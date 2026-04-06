package com.ecotrack.livepoll.controller;

import com.ecotrack.livepoll.auth.AppUserPrincipal;
import com.ecotrack.livepoll.dto.PollDtos;
import com.ecotrack.livepoll.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping
    public ResponseEntity<List<PollDtos.PollResponse>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDtos.PollResponse> getPoll(@PathVariable Long id) {
        return ResponseEntity.ok(pollService.getPollById(id));
    }

    @PostMapping("/{id}/vote")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PollDtos.PollResponse> vote(@PathVariable Long id,
                                                      @Valid @RequestBody PollDtos.VoteRequest request,
                                                      @AuthenticationPrincipal AppUserPrincipal principal) {
        return ResponseEntity.ok(pollService.vote(id, request.optionId(), principal.getUsername()));
    }
}
