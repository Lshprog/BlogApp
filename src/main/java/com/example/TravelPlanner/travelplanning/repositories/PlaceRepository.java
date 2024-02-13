package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.entities.Place;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Set<Place> findPlacesByTravelPlan(TravelPlan travelPlan);

}
