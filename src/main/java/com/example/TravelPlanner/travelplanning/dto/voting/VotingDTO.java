package com.example.TravelPlanner.travelplanning.dto.voting;

import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingDTO implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String creator;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private EventDTO event;

    private Integer likes;
    private Integer dislikes;

}
