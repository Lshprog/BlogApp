package com.example.TravelPlanner.travelplanning.dto.voting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingPreviewDTO {

    private Long id;

    private String creator;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
