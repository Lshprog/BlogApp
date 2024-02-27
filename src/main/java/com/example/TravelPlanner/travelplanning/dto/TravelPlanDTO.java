package com.example.TravelPlanner.travelplanning.dto;

import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// Dto for the list of travel plans to show on the front page for each user
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanDTO {

    private Long id;

    private String ownerUsername;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<EventDTO> events;

    private String joinCode;
}
