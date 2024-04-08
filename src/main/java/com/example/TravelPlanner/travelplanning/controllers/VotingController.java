package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.voting.*;
import com.example.TravelPlanner.travelplanning.services.VotingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
public class VotingController {

    private final VotingService votingService;
    @GetMapping("/{votingId}")
    public ResponseEntity<VotingDTO> getVotingById(@PathVariable Long votingId,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(votingService.getVotingById(votingId, customUserDetails.getId()));
    }

    @GetMapping
    public ResponseEntity<List<VotingDTO>> getVotings(@PathVariable Long travelPlanId,
                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(votingService.getVotingsByTravelPlan(travelPlanId, customUserDetails.getId()));
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
    public ResponseEntity<Void> deleteVoting(@PathVariable Long votingId) {
        // Implementation
        votingService.deleteVoting(votingId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearVotingHistory(@PathVariable Long travelPlanId) {
        votingService.deleteFinishedVotings(travelPlanId);
        return ResponseEntity.ok().build();
    }

    // Make vote
    @PostMapping("/{votingId}/vote")
    public ResponseEntity<Void> makeVote(@PathVariable Long votingId,
                                         @RequestBody VoteCreateDTO voteDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        votingService.makeVote(voteDto, votingId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }
}
