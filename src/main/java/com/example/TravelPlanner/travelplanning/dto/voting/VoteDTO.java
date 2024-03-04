package com.example.TravelPlanner.travelplanning.dto.voting;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO implements Serializable {

    private String creator;

    private Long votingId;

    private String description;

}