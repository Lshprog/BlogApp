package com.example.TravelPlanner.travelplanning.controllers;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
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
//    @GetMapping
//    public ResponseEntity<List<EventDTO>> getEvents(@PathVariable Long travelPlanId) {
//        // Implementation
//        return ResponseEntity.ok().body(travellingService.get());
//    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@PathVariable Long travelPlanId,
                                                @RequestBody EventCreateDTO eventDTO,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(eventService.saveNewEvent(eventDTO, customUserDetails.getId(), travelPlanId));
    }


    // Update event
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long travelPlanId,
                                                @PathVariable Long eventId,
                                                @RequestBody EventDTO eventDto) {
        eventService.updateEvent(eventDto, travelPlanId);
        return ResponseEntity.ok().build();
    }

    // Delete event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }
}
