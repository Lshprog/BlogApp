package com.example.TravelPlanner.travelplanning.dto.voting;

import com.example.TravelPlanner.travelplanning.entities.Vote;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingCreateDTO {
    @NotNull
    private Long eventId;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
}
