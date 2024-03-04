package com.example.TravelPlanner.travelplanning.dto.travelplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanShowDTO implements Serializable {
    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

}
