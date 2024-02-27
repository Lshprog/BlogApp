package com.example.TravelPlanner.travelplanning.dto;

import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// To show every even on the front page for every travel plan

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private Long id;

    private String creator;

    private String title;

    private Long travelPlanId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    private PlaceStatus placeStatus;

    private Location location;

    private VotingDTO voting;
}
