package com.example.TravelPlanner.travelplanning.dto.voting;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteCreateDTO implements Serializable {
    @NotNull
    private Boolean isLiked;
}
