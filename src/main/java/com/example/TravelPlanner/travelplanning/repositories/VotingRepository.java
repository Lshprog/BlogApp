package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.entities.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface VotingRepository extends JpaRepository<Voting, Long> {

    @Query("SELECT vtg FROM Voting vtg WHERE vtg.event.travelPlan.id = :travelPlanId")
    List<Voting> findVotingsByTravelPlanId(Long travelPlanId);

}
