package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findEventsByTravelPlanId(Long planId);

    @Modifying
    @Query("UPDATE Event e SET e.placeStatus = :newStatus WHERE e.travelPlan.id = :travelPlanId AND e.placeStatus = :currentStatus AND e.startTime > :startTime AND e.endTime < :endTime")
    void updateEventStatusByTravelPlanAndTime(@Param("travelPlanId") Long travelPlanId,
                                             @Param("currentStatus") PlaceStatus currentStatus,
                                             @Param("newStatus") PlaceStatus newStatus,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    @Query("SELECT e FROM Event e WHERE e.placeStatus = :currentStatus AND e.travelPlan.id = :travelPlanId AND e.startTime > :startTime AND e.endTime < :endTime")
    List<Event> findEventsByTravelPlanAndTimeAndPlaceStatus(@Param("travelPlanId") Long travelPlanId,
                                              @Param("currentStatus") PlaceStatus currentStatus,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    List<Event> findEventsByTravelPlanIdAndPlaceStatus(Long travelPlanId, PlaceStatus placeStatus);

    @Query("SELECT e FROM Event e WHERE e.travelPlan.id = :travelPlanId AND e.placeStatus <> :excludedStatus")
    List<Event> findNotSomePlaceStatusEvents(@Param("travelPlanId") Long travelPlanId, @Param("excludedStatus") PlaceStatus excludedStatus);

}
