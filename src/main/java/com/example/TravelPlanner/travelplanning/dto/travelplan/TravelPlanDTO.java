package com.example.TravelPlanner.travelplanning.dto.travelplan;

import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanDTO implements Serializable {

    private Long id;

    private String ownerUsername;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<EventDTO> events;

    private String joinCode;
}
