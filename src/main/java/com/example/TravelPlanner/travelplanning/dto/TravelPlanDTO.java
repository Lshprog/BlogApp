package com.example.TravelPlanner.travelplanning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanDTO {

    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String joinCode;

}
