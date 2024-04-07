package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanPreviewDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanUpdateDTO;
import com.example.TravelPlanner.travelplanning.services.TravelPlanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/travelplans")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    @GetMapping
    public ResponseEntity<List<TravelPlanPreviewDTO>> getAllTravelPlansForUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println(customUserDetails.getId().toString());
        return ResponseEntity.ok().body(travelPlanService.listAllTravelPlansByUser(customUserDetails.getId()));
    }

    @GetMapping("/{travelPlanId}")
    public ResponseEntity<TravelPlanDTO> getTravelPlanById(@PathVariable Long travelPlanId,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travelPlanService.getTravelPlanById(travelPlanId));
    }

    @PostMapping
    public ResponseEntity<TravelPlanDTO> createTravelPlan(@RequestBody TravelPlanCreateDTO travelPlanCreateDTO,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travelPlanService.saveNewTravelPlan(travelPlanCreateDTO, customUserDetails.getId()));
    }

    @PostMapping("/{travelPlanId}/joinlink")
    public ResponseEntity<String> generateJoinLink(@PathVariable Long travelPlanId,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travelPlanService.generateNewInviteLink(travelPlanId, customUserDetails.getId()));
    }

    @PostMapping("/join/{joinLink}")
    public ResponseEntity<TravelPlanDTO> joinTravelPlanByLink(@PathVariable String joinLink,
                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(travelPlanService.joinTravelPlan(joinLink, customUserDetails.getId()));
    }

    @PutMapping("/{travelPlanId}")
    public ResponseEntity<Void> updateTravelPlan(@PathVariable Long travelPlanId,
                                                 @RequestBody TravelPlanUpdateDTO travelPlanUpdateDTO,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        travelPlanService.updateTravelPlan(travelPlanUpdateDTO, travelPlanId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{travelPlanId}")
    public ResponseEntity<Void> deleteTravelPlan(@PathVariable Long travelPlanId,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        travelPlanService.deleteTravelPlan(travelPlanId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }
}
