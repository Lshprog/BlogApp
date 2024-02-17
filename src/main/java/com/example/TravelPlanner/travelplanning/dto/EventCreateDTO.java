package com.example.TravelPlanner.travelplanning.dto;

import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDTO {
    @NotBlank
    private String title;

    private Long travelPlanId;

    @NotBlank
    private LocalDateTime startTime;

    @NotBlank
    private LocalDateTime endTime;

    private String description;

    @NotBlank
    private PlaceStatus placeStatus;

    @NotBlank
    private Location location;
}