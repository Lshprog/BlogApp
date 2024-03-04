package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanShowDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanUpdateDTO;
import com.example.TravelPlanner.travelplanning.services.TravellingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/travelplans")
@RequiredArgsConstructor
public class TravelPlanController {

    private final TravellingService travellingService;

    @GetMapping
    public ResponseEntity<List<TravelPlanShowDTO>> getAllTravelPlansForUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travellingService.listAllTravelPlansByUser(customUserDetails.getId()));
    }

    @GetMapping("/{travelPlanId}")
    public ResponseEntity<TravelPlanDTO> getTravelPlanById(@PathVariable Long travelPlanId,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travellingService.getTravelPlanById(travelPlanId));
    }

    @PostMapping
    public ResponseEntity<TravelPlanDTO> createTravelPlan(@RequestBody TravelPlanCreateDTO travelPlanCreateDTO,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travellingService.saveNewTravelPlan(travelPlanCreateDTO, customUserDetails.getId()));
    }

    @PostMapping("/{travelPlanId}/joinlink")
    public ResponseEntity<String> generateJoinLink(@PathVariable Long travelPlanId) {
        return ResponseEntity.ok().body(travellingService.generateNewInviteLink(travelPlanId));
    }

    @PostMapping("/join")
    public ResponseEntity<TravelPlanDTO> joinTravelPlanByLink(@RequestBody String joinLink,
                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travellingService.joinTravelPlan(joinLink, customUserDetails.getId()));
    }

    @PutMapping("/{travelPlanId}")
    public ResponseEntity<TravelPlanDTO> updateTravelPlan(@RequestBody TravelPlanUpdateDTO travelPlanUpdateDTO,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travellingService.updateTravelPlan(travelPlanUpdateDTO, customUserDetails.getId()));
    }

    @DeleteMapping("/{travelPlanId}")
    public ResponseEntity<Void> deleteTravelPlan(@PathVariable Long travelPlanId,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        travellingService.deleteTravelPlan(travelPlanId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }
}
