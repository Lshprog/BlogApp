package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.EventDTO;
import com.example.TravelPlanner.travelplanning.dto.VoteDTO;
import com.example.TravelPlanner.travelplanning.dto.VotingDTO;
import com.example.TravelPlanner.travelplanning.services.TravellingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/travelplans/{travelPlanId}/voting")
@RequiredArgsConstructor
public class VotingController {
    // Show voting by id
    private final TravellingService travellingService;
    @GetMapping("/{votingId}")
    public ResponseEntity<VotingDTO> getVotingById(@PathVariable Long votingId) {
        // Implementation
        return ResponseEntity.ok().body(travellingService.getVotingById(votingId));
    }

//    @GetMapping
//    public ResponseEntity<List<VotingDTO>> getVotings(@PathVariable Long eventId) {
//        // Implementation
//        return ResponseEntity.ok().body(new ArrayList<>());
//    }

    @PostMapping
    public ResponseEntity<VotingDTO> createVoting(VotingDTO votingDTO,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // Implementation
        return ResponseEntity.ok().body(travellingService.createNewVoting(votingDTO, customUserDetails.getId()));
    }

    // Update voting
    @PutMapping("/{votingId}")
    public ResponseEntity<VotingDTO> updateVoting(@PathVariable Long votingId, @RequestBody VotingDTO votingDto) {
        // Implementation
        return ResponseEntity.ok().body(new VotingDto());
    }

    // Delete voting
    @DeleteMapping("/{votingId}")
    public ResponseEntity<Void> deleteVoting(@PathVariable Long votingId) {
        // Implementation
        travellingService.deleteVoting(votingId);
        return ResponseEntity.noContent().build();
    }

    // Make vote
    @PostMapping("/{votingId}/vote")
    public ResponseEntity<Void> makeVote(@PathVariable Long votingId, @RequestBody VoteDTO voteDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // Implementation
        return ResponseEntity.ok().build();
    }
}
