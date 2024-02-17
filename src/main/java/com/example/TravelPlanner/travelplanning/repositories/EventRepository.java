package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    Set<Event> findEventsByTravelPlan(TravelPlan travelPlan);

}
