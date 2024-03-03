package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotesRepository extends JpaRepository<Vote, Long> {

    Vote findByCreatorAndVoting(User creator, Voting voting);

}
