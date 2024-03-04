package com.example.TravelPlanner.travelplanning.dto.voting;

import com.example.TravelPlanner.travelplanning.entities.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingCreateDTO {

    private String creator;

    private Long eventId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
