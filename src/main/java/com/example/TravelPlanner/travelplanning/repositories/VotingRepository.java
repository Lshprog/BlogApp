package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.entities.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotingRepository extends JpaRepository<Voting, Long> {

    @Query("SELECT vtg FROM Voting vtg WHERE vtg.event.travelPlan.id = :travelPlanId")
    List<Voting> findVotingsByTravelPlanId(Long travelPlanId);

    @Modifying
    @Query(value = "DELETE FROM Voting v WHERE v.event.travelPlan.id = :travelPlanId AND v.endTime < CURRENT_TIMESTAMP")
    void deleteFinishedVotings(Long travelPlanId);

}
