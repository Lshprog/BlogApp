package com.example.TravelPlanner.travelplanning.dto;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingDTO {

    private Long id;

    private String creator;

    private EventDTO event;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<Vote> votes;

}
