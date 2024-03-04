package com.example.TravelPlanner.travelplanning.dto.voting;

import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
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

    private Long id;

    private String creator;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private EventDTO event;

    private List<VoteDTO> votes;

}
