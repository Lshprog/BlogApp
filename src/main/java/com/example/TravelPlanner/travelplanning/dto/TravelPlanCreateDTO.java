package com.example.TravelPlanner.travelplanning.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanCreateDTO {
    @NotBlank
    private String title;

    @NotBlank
    private LocalDate startDate;

    @NotBlank
    private LocalDate endDate;

}