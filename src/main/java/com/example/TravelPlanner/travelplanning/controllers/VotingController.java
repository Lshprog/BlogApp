package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.common.utils.annotations.ValidVotingId;
import com.example.TravelPlanner.travelplanning.dto.voting.*;
import com.example.TravelPlanner.travelplanning.services.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/travelplans/{travelPlanId}/voting")
@RequiredArgsConstructor
@Validated
public class VotingController {

    private final VotingService votingService;
    @GetMapping("/{votingId}")
    public ResponseEntity<VotingDTO> getVotingById(@ValidVotingId @PathVariable Long votingId) {
        return ResponseEntity.ok().body(votingService.getVotingById(votingId));
    }

    @GetMapping
    public ResponseEntity<List<VotingPreviewDTO>> getVotings(@PathVariable Long travelPlanId) {
        return ResponseEntity.ok().body(votingService.getVotingsByTravelPlan(travelPlanId));
    }

    @PostMapping
    public ResponseEntity<VotingDTO> createVoting(@RequestBody VotingCreateDTO votingDTO,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(votingService.createNewVoting(votingDTO, customUserDetails.getId()));
    }

    // Update voting
//    @PutMapping("/{votingId}")
//    public ResponseEntity<VotingDTO> updateVoting(@PathVariable Long votingId, @RequestBody VotingDTO votingDto) {
//        // Implementation
//        return ResponseEntity.ok().body(new VotingDto());
//    }

    // Delete voting
    @DeleteMapping("/{votingId}")
    public ResponseEntity<Void> deleteVoting(@ValidVotingId @PathVariable Long votingId) {
        // Implementation
        votingService.deleteVoting(votingId);
        return ResponseEntity.ok().build();
    }

    // Make vote
    @PostMapping("/{votingId}/vote")
    public ResponseEntity<Void> makeVote(@PathVariable @ValidVotingId Long votingId,
                                         @RequestBody VoteCreateDTO voteDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        votingService.makeVote(voteDto, votingId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }
}
