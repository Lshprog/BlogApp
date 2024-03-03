package com.example.TravelPlanner.travelplanning.common.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {
    private String name;
    private Double lat;
    private Double lon;
}
