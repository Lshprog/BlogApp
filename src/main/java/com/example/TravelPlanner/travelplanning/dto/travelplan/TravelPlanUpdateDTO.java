package com.example.TravelPlanner.travelplanning.dto.travelplan;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanUpdateDTO implements Serializable {

    @NotBlank
    private String title;

    @NotBlank
    private LocalDate startDate;

    @NotBlank
    private LocalDate endDate;

}
