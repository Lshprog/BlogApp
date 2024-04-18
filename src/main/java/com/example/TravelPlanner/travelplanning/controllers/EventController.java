package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/travelplans/{travelPlanId}/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    // Show event by id
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok().body(eventService.getEventById(eventId));
    }

    @GetMapping("/concrete")
    public ResponseEntity<List<EventDTO>> getConcreteEvents(@PathVariable Long travelPlanId) {
        return ResponseEntity.ok().body(eventService.getEvents("CONCRETE", travelPlanId));
    }

    @GetMapping("/suggested")
    public ResponseEntity<List<EventDTO>> getSuggestedEvents(@PathVariable Long travelPlanId) {
        return ResponseEntity.ok().body(eventService.getEvents("SUGGESTED", travelPlanId));
    }

    @GetMapping("/concrete/{day}")
    public ResponseEntity<List<EventDTO>> getConcreteEventsByDay(@PathVariable Long travelPlanId,
                                                                 @PathVariable LocalDate day) {
        return ResponseEntity.ok().body(eventService.getEventsByDay("CONCRETE", travelPlanId, day));
    }

    @GetMapping("/suggested/{day}")
    public ResponseEntity<List<EventDTO>> getSuggestedEventsByDay(@PathVariable Long travelPlanId,
                                                                  @PathVariable LocalDate day) {
        return ResponseEntity.ok().body(eventService.getEventsByDay("SUGGESTED", travelPlanId, day));
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@PathVariable Long travelPlanId,
                                                @RequestBody EventCreateDTO eventDTO,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(eventService.saveNewEvent(eventDTO, travelPlanId, customUserDetails.getId()));
    }


    // Update event
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long travelPlanId,
                                                @PathVariable Long eventId,
                                                @RequestBody EventDTO eventDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        eventService.updateEvent(eventDto, travelPlanId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }

    // Delete event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        eventService.deleteEvent(eventId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }
}
