package com.example.TravelPlanner.travelplanning.dto.event;

import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDTO implements Serializable {
    @NotBlank
    private String title;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String description;

    @NotNull
    private PlaceStatus placeStatus;

    @NotNull
    private String location;
}