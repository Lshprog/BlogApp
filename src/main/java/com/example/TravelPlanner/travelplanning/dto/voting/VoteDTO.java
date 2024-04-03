package com.example.TravelPlanner.travelplanning.dto.voting;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDTO implements Serializable {

    private String creator;

    private Boolean isLiked;

}
